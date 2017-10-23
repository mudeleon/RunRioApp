package tip.edu.ph.runrio.ui.runner.list;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.databinding.ActivityRunnerListBinding;
import tip.edu.ph.runrio.model.data.Profile;
import tip.edu.ph.runrio.model.data.User;
import tip.edu.ph.runrio.ui.runner.detail.RunnerDetailActivity;


public class RunnerListActivity extends MvpViewStateActivity<RunnerListView, RunnerListPresenter>
        implements RunnerListView, SwipeRefreshLayout.OnRefreshListener {

    private ActivityRunnerListBinding binding;
    private Realm realm;
    private User user;
    private RunnerListAdapter adapterRunnerList;
    private RealmResults<Profile> profileRealmResults;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_runner_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Runner List");


        binding.swipeRefreshLayout.setOnRefreshListener(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0)
                        ? 0 : recyclerView.getChildAt(0).getTop();
                binding.swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }
        });

        adapterRunnerList = new RunnerListAdapter(this, getMvpView());
        binding.recyclerView.setAdapter(adapterRunnerList);


    }


    public void prepareList() {
        String searchText = ((RunnerListViewState) getViewState()).getSearchText();
        if (profileRealmResults.isLoaded() && profileRealmResults.isValid()) {
            List<Profile> AdminList;
            if (searchText.isEmpty()) {
                AdminList = realm.copyFromRealm(profileRealmResults);
            } else {
                AdminList = realm.copyFromRealm(profileRealmResults.where()
                        .contains("profileFirstName", searchText, Case.INSENSITIVE)
                        .or()
                        .contains("profileLastName", searchText, Case.INSENSITIVE)
                        .findAll());
            }
            adapterRunnerList.setRunnerList(AdminList);
            adapterRunnerList.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        String searchText = ((RunnerListViewState) getViewState()).getSearchText();
        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ((RunnerListViewState) getViewState()).setSearchText(newText);
                prepareList();
                return true;
            }
        });
        if (!searchText.isEmpty()) {
            search.setIconified(false);
            search.setQuery(searchText, true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        loadData();
    }

    @Override
    protected void onDestroy() {
        profileRealmResults.removeChangeListeners();
        realm.close();
        super.onDestroy();
    }

    @NonNull
    @Override
    public RunnerListPresenter createPresenter() {
        return new RunnerListPresenter();
    }

    @NonNull
    @Override
    public ViewState<RunnerListView> createViewState() {
        setRetainInstance(true);
        return new RunnerListViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        ((RunnerListViewState) getViewState()).setSearchText("");
    }


    public void loadData()
    {
        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        profileRealmResults = realm.where(Profile.class).findAll();
            if (profileRealmResults.isLoaded() && profileRealmResults.isValid()) {
                getMvpView().setRunnerList();
            }else
            {

                presenter.loadRunner(user.getApiToken());
            }
    }


    @Override
    public void setRunnerList() {
        profileRealmResults = realm.where(Profile.class).findAllAsync();
        adapterRunnerList.setRunnerList(realm.copyToRealmOrUpdate( profileRealmResults.where()
                .findAll()));
        adapterRunnerList.notifyDataSetChanged();

    }

    @Override
    public void stopRefresh() {
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String message) {
        new AlertDialog.Builder(this)
                .setTitle(message)
                .setPositiveButton("Close", null)
                .show();
    }

    @Override
    public void onRunnerClicked(Profile runner) {
        Intent intent = new Intent(this, RunnerDetailActivity.class);
       // intent.putExtra(Constants.ID, Admin.getAdminID());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {

        presenter.loadRunner(user.getApiToken());

    }
}
