package tip.edu.ph.runrio.ui.upcoming_race.transaction.racekit;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import io.realm.Realm;
import tip.edu.ph.runrio.BuildConfig;
import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.app.Constants;
import tip.edu.ph.runrio.databinding.ActivityTransactionRaceTypeBinding;
import tip.edu.ph.runrio.model.data.RaceType;
import tip.edu.ph.runrio.model.data.UpcomingRaces;
import tip.edu.ph.runrio.model.data.User;
import tip.edu.ph.runrio.ui.upcoming_race.transaction.racetype.RaceTypeSingleAdapter;


public class RaceKitTransactionActivity extends MvpViewStateActivity<RaceKitTransactionView, RaceKitTransactionPresenter> implements RaceKitTransactionView {

    private static final String TAG = RaceKitTransactionActivity.class.getSimpleName();
    private ActivityTransactionRaceTypeBinding binding;
    private Realm realm;
    private User user;
    private UpcomingRaces races;
    private ProgressDialog progressDialog;
    private String upcomingID;
    SupportMapFragment mapFragment;
    private RaceKitMultipleAdapter racetypeMultipleAdapter;
    private RaceTypeSingleAdapter racetypeSingleAdapter;



    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_race_type);
     //   binding.setView(getMvpView());
        //setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(BuildConfig.DEBUG ? "Upcoming Races" : "Upcoming Races");


        upcomingID = getIntent().getStringExtra(Constants.UPCOMING_ID);
        if (upcomingID.equalsIgnoreCase("")) {
            Log.d(TAG, "no int extra found");
            Toast.makeText(this, "Unable to get Event Details", Toast.LENGTH_SHORT).show();
            finish();
        }

        init();


    }

    public void init()
    {

        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");
        progressDialog.setCancelable(false);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        binding.recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        //racetypeSingleAdapter = new RaceTypeSingleAdapter(this, getMvpView());
        binding.recyclerView.setAdapter(racetypeSingleAdapter);



        binding.recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView2.setItemAnimator(new DefaultItemAnimator());
        racetypeMultipleAdapter = new RaceKitMultipleAdapter(this, getMvpView());
        binding.recyclerView2.setAdapter(racetypeMultipleAdapter);

        races = realm.where(UpcomingRaces.class).findFirst();
        if (races.isLoaded() && races.isValid()) {
            getMvpView().setUpcomingRaces();
        }else
        {
            Log.d(TAG, "load>> user");
            presenter.loadUpcomingRaces(user.getApiToken(),upcomingID);
        }

    }

    @Override
    public void setUpcomingRaces() {


        races = realm.where(UpcomingRaces.class).findFirst();
        binding.setRace(races);

        racetypeMultipleAdapter.setUpcomingRaces(realm.copyFromRealm(races.getRacetypeCategory().where()
                .findAll()));
        racetypeMultipleAdapter.notifyDataSetChanged();

        racetypeSingleAdapter.setUpcomingRaces(realm.copyFromRealm(races.getRacetypeCategory().where()
                .findAll()));
        racetypeSingleAdapter.notifyDataSetChanged();

    }

    @Override
    public void switchView(boolean check) {


        if(check) {
            binding.runnerOne.setVisibility(View.GONE);
            binding.runnerMany.setVisibility(View.VISIBLE);
        }else
        {
            binding.runnerOne.setVisibility(View.VISIBLE);
            binding.runnerMany.setVisibility(View.GONE);
        }

    }


    @Override
    public void soloRace(RaceType race) {



        Log.d(">>>",racetypeMultipleAdapter.getListValue().get(0)+"");
    }

    @Override
    public void clickNext() {


        Log.d(">>>",racetypeMultipleAdapter.getListValue().get(0)+"");
    }





    @Override
    public void onResume() {
        super.onResume();

        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        races = realm.where(UpcomingRaces.class).findFirst();
        if (!(races.isLoaded() && races.isValid())) {
            getMvpView().setUpcomingRaces();
        }else
        {
            presenter.loadUpcomingRaces(user.getApiToken(),(upcomingID));
            progressDialog.show();
        }
    }


    @Override
    protected void onDestroy() {
//        races.removeChangeListeners();


        realm.close();
        super.onDestroy();
    }


    @NonNull
    @Override
    public RaceKitTransactionPresenter createPresenter() {
        return new RaceKitTransactionPresenter();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_refresh:
                onRefresh();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onRefresh() {
            presenter.loadUpcomingRaces(user.getApiToken(),(upcomingID));
        progressDialog.show();

    }

    @Override
    public void stopRefresh() {
        progressDialog.dismiss();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



    @NonNull
    @Override
    public ViewState<RaceKitTransactionView> createViewState() {
        setRetainInstance(true);
        return new RaceKitTransactionViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }
}
