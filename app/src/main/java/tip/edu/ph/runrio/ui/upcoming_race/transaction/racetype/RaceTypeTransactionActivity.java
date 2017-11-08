package tip.edu.ph.runrio.ui.upcoming_race.transaction.racetype;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import io.realm.Realm;
import io.realm.RealmResults;
import tip.edu.ph.runrio.BuildConfig;
import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.app.Constants;
import tip.edu.ph.runrio.app.Endpoints;
import tip.edu.ph.runrio.databinding.ActivityTransactionRaceTypeBinding;
import tip.edu.ph.runrio.databinding.ActivityUpcomingRaceDetailBinding;
import tip.edu.ph.runrio.databinding.DialogUpcomingdetailwebviewBinding;
import tip.edu.ph.runrio.model.data.Profile;
import tip.edu.ph.runrio.model.data.RaceType;
import tip.edu.ph.runrio.model.data.UpcomingRaces;
import tip.edu.ph.runrio.model.data.User;
import tip.edu.ph.runrio.ui.upcoming_race.detail.UpcomingRaceDetailPresenter;
import tip.edu.ph.runrio.ui.upcoming_race.transaction.profiletab.ProfileTabActivity;


public class RaceTypeTransactionActivity extends MvpViewStateActivity<RaceTypeTransactionView, RaceTypeTransactionPresenter> implements RaceTypeTransactionView {

    private static final String TAG = RaceTypeTransactionActivity.class.getSimpleName();
    private ActivityTransactionRaceTypeBinding binding;
    private Realm realm;
    private User user;
    private UpcomingRaces races;
    private ProgressDialog progressDialog;
    private String upcomingID;
    SupportMapFragment mapFragment;
    private RealmResults<Profile> profileRealmResults;
    private RaceTypeMultipleAdapter racetypeMultipleAdapter;
    private RaceTypeSingleAdapter racetypeSingleAdapter;
    private String ctr;



    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_race_type);
        binding.setView(getMvpView());
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
        racetypeSingleAdapter = new RaceTypeSingleAdapter(this, getMvpView());
        binding.recyclerView.setAdapter(racetypeSingleAdapter);



        binding.recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView2.setItemAnimator(new DefaultItemAnimator());
        racetypeMultipleAdapter = new RaceTypeMultipleAdapter(this, getMvpView());
        binding.recyclerView2.setAdapter(racetypeMultipleAdapter);

        races = realm.where(UpcomingRaces.class).findFirst();
        if (races.isLoaded() && races.isValid()) {
            getMvpView().setUpcomingRaces();
        }else
        {
            Log.d(TAG, "load>> user");
            presenter.loadUpcomingRaces(user.getApiToken(),upcomingID);
            presenter.loadRunner(user.getApiToken());
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
            binding.runnerManyNext.setVisibility(View.VISIBLE);
        }else
        {
            binding.runnerOne.setVisibility(View.VISIBLE);
            binding.runnerMany.setVisibility(View.GONE);
            binding.runnerManyNext.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void soloRace(RaceType race) {

        Intent intent = new Intent(this, ProfileTabActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString(Constants.UPCOMING_ID, upcomingID);
        mBundle.putString(Constants.RACE_TYPE_SINGLE, race.getId());
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    @Override
    public void clickNext() {

        boolean nullchecker=false;
        ctr="";
        int totalRunner=0;
        for(int a=0;a<racetypeMultipleAdapter.getItemCount();a++)
        {
            if(!(racetypeMultipleAdapter.getListValue().get(a)==0))
            {
                nullchecker = true;
            }

            totalRunner += racetypeMultipleAdapter.getListValue().get(a);

            ctr += (racetypeMultipleAdapter.getListValue().get(a)+"%");
        }
        Intent intent = new Intent(this, ProfileTabActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString(Constants.UPCOMING_ID, upcomingID);
        mBundle.putString(Constants.RACE_TYPE_MULTIPLE, ctr);
        mBundle.putInt(Constants.RUNNER_TOTAL, totalRunner);
        mBundle.putString(Constants.RACE_TYPE_SINGLE, "");
        intent.putExtras(mBundle);

        profileRealmResults = realm.where(Profile.class).contains("profileActive","Y").findAllSorted("id");
        if(totalRunner<=profileRealmResults.size()) {
            if (nullchecker)
                startActivity(intent);
            else
                showError("No Runner Selected!");
        }else
        {
           // startActivity(intent);
            showError("Total Participants is more than the registered runner!");
        }
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
            presenter.loadRunner(user.getApiToken());
            progressDialog.show();
        }
    }

    @Override
    protected void onDestroy() {

        realm.close();
        super.onDestroy();
    }

    @NonNull
    @Override
    public RaceTypeTransactionPresenter createPresenter() {
        return new RaceTypeTransactionPresenter();
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
    public ViewState<RaceTypeTransactionView> createViewState() {
        setRetainInstance(true);
        return new RaceTypeTransactionViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
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



}
