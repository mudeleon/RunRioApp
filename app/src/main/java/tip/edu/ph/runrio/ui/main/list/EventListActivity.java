package tip.edu.ph.runrio.ui.main.list;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.app.Constants;
import tip.edu.ph.runrio.databinding.ActivityEventListBinding;
import tip.edu.ph.runrio.model.data.RacesResult;
import tip.edu.ph.runrio.model.data.Reservation;
import tip.edu.ph.runrio.model.data.UpcomingRaces;
import tip.edu.ph.runrio.model.data.User;
import tip.edu.ph.runrio.ui.main.list.RaceResultListAdapter;
import tip.edu.ph.runrio.ui.main.list.UpcomingRacesListAdapter;
import tip.edu.ph.runrio.ui.main.list.UserRacesListAdapter;
import tip.edu.ph.runrio.ui.upcoming_race.detail.UpcomingRaceDetailActivity;
import tip.edu.ph.runrio.ui.upcoming_race.transaction.racetype.RaceTypeTransactionActivity;


public class EventListActivity
        extends MvpViewStateActivity<EventListView, EventListPresenter>
        implements SwipeRefreshLayout.OnRefreshListener, EventListView {

    private static final String TAG = EventListActivity.class.getSimpleName();
    private ActivityEventListBinding binding;
    private Realm realm;
    private User user;
    private RealmResults<UpcomingRaces> upcomingRacesRealmResults;
    private RealmResults<RacesResult> racesResultRealmResults;
    private RealmResults<Reservation> reservationRealmResults;
    private UpcomingRacesListAdapter upcomingRacesListAdapter;
    private UserRacesListAdapter userRacesListAdapter;
    private RaceResultListAdapter raceResultListAdapter;
    private String searchText;
    public String id;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        searchText = "";
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();

        if (user == null) {
            Log.d(TAG, "No User found");
            finish();
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         id = getIntent().getStringExtra("listIntent");
        Log.d(TAG, "id>> "+id);
        if (id .equalsIgnoreCase("")) {
            Log.d("TAG", "no int extra found");
            Toast.makeText(this, "Error Getting List", Toast.LENGTH_SHORT).show();
            finish();
        }

        if(id.equalsIgnoreCase("m"))
        {
            getSupportActionBar().setTitle("My Races");
            userRacesListAdapter = new tip.edu.ph.runrio.ui.main.list.UserRacesListAdapter(this, getMvpView());
            binding.recyclerView.setAdapter(userRacesListAdapter);

        } else  if(id.equalsIgnoreCase("u"))
        {

            getSupportActionBar().setTitle("Upcoming Races");
            upcomingRacesListAdapter = new tip.edu.ph.runrio.ui.main.list.UpcomingRacesListAdapter(this, getMvpView());
            binding.recyclerView.setAdapter(upcomingRacesListAdapter);



        }else  if(id.equalsIgnoreCase("r"))
        {
            getSupportActionBar().setTitle("Races Result");
            raceResultListAdapter = new RaceResultListAdapter(this, getMvpView());
            binding.recyclerView.setAdapter(raceResultListAdapter);

        }



        //binding.swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_layout_color_scheme));
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0)
                        ? 0 : recyclerView.getChildAt(0).getTop();
                binding.swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText = newText;
                //prepareList();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /*private void prepareList() {
        if (eventRealmResults.isLoaded() && eventRealmResults.isValid()) {
            if (searchText.isEmpty()) {
                getMvpView().setData(realm.copyFromRealm(eventRealmResults));
            } else {
                getMvpView().setData(realm.copyToRealmOrUpdate(eventRealmResults.where()
                        .contains("eventName", searchText, Case.INSENSITIVE)
                        .or()
                        .contains("tags", searchText, Case.INSENSITIVE)
                        .findAll()));
            }
        }
    }*/

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
        racesResultRealmResults.removeChangeListeners();
        reservationRealmResults.removeChangeListeners();
        upcomingRacesRealmResults.removeChangeListeners();


        realm.close();
        super.onDestroy();
    }

    @NonNull
    @Override
    public EventListPresenter createPresenter() {
        return new EventListPresenter();
    }

    @Override
    public void onRefresh() {
        if(id.equalsIgnoreCase("m"))
        {
            presenter.loadUserRaces(user.getApiToken());

        } else  if(id.equalsIgnoreCase("u"))
        {

            presenter.loadUpcomingRaces(user.getApiToken());

        }else  if(id.equalsIgnoreCase("r"))
        {
            presenter.loadRacesResult(user.getApiToken());

        }



    }
    public void loadData()
    {
        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        reservationRealmResults = realm.where(Reservation.class).findAll();
        racesResultRealmResults = realm.where(RacesResult.class).findAll();
        upcomingRacesRealmResults = realm.where(UpcomingRaces.class).findAll();


        if(id.equalsIgnoreCase("m"))
        {
            if (reservationRealmResults.isLoaded() && reservationRealmResults.isValid()) {
                getMvpView().setUserRaces();
            }else
            {

                presenter.loadUserRaces(user.getApiToken());
            }

        } else  if(id.equalsIgnoreCase("u"))
        {


            if (!(upcomingRacesRealmResults.isLoaded() && reservationRealmResults.isValid())) {
                getMvpView().setUpcomingRaces();
            }else
            {
                presenter.loadUpcomingRaces(user.getApiToken());
            }


        }else  if(id.equalsIgnoreCase("r"))
        {
            if (racesResultRealmResults.isLoaded() && racesResultRealmResults.isValid()) {
                getMvpView().setRacesResult();
            }else
            {
                presenter.loadRacesResult(user.getApiToken());
            }

        }





    }

    @Override
    public void setRacesResult(){

        racesResultRealmResults = realm.where(RacesResult.class).findAllAsync();
        raceResultListAdapter.setRacesResult(realm.copyToRealmOrUpdate(racesResultRealmResults.where()
                .findAll()));//Sorted("eventDateFrom", Sort.ASCENDING)));
        raceResultListAdapter.notifyDataSetChanged();
    }

    @Override
    public void setUpcomingRaces() {


        upcomingRacesRealmResults = realm.where(UpcomingRaces.class).findAllAsync();
        upcomingRacesListAdapter.setUpcomingRaces(realm.copyFromRealm(upcomingRacesRealmResults.where()
                .findAll()));//Sorted("eventDateFrom", Sort.ASCENDING))););
        upcomingRacesListAdapter.notifyDataSetChanged();
    }


    @Override
    public void setUserRaces() {
        reservationRealmResults = realm.where(Reservation.class).findAllAsync();

        userRacesListAdapter.setUserRaces(realm.copyFromRealm(reservationRealmResults.where()
                .findAll()));
        userRacesListAdapter.notifyDataSetChanged();
    }


    @Override
    public void stopRefresh() {
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUpcomingRaces(UpcomingRaces eventUpcomingRaces) {

        Intent intent = new Intent(this, RaceTypeTransactionActivity.class);
        intent.putExtra(Constants.UPCOMING_ID, eventUpcomingRaces.getId());
        startActivity(intent);


    }

    @Override
    public void showRacesResult(RacesResult eventRacesResult) {

    }

    @Override
    public void showUserRaces(Reservation eventReservation) {

    }


    @Override
    public void showRacesResultDetails(RacesResult eventRacesResult) {

    }

    @Override
    public void showUpcomingRacesDetails(UpcomingRaces eventUpcomingRaces) {
        Intent intent = new Intent(this, UpcomingRaceDetailActivity.class);
        intent.putExtra(Constants.UPCOMING_ID, eventUpcomingRaces.getId());
        startActivity(intent);
    }

    @Override
    public void showUserRacesDetails(Reservation eventReservation) {

    }



    @NonNull
    @Override
    public ViewState<EventListView> createViewState() {
        setRetainInstance(true);
        return new EventListViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        binding.swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                binding.swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }
}
