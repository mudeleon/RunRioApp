package tip.edu.ph.runrio.ui.upcoming_race.detail;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import tip.edu.ph.runrio.BuildConfig;
import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.app.Constants;
import tip.edu.ph.runrio.app.Endpoints;
import tip.edu.ph.runrio.databinding.ActivityUpcomingRaceDetailBinding;
import tip.edu.ph.runrio.databinding.DialogUpcomingdetailwebviewBinding;
import tip.edu.ph.runrio.model.data.RacesResult;
import tip.edu.ph.runrio.model.data.Reservation;
import tip.edu.ph.runrio.model.data.UpcomingRaces;
import tip.edu.ph.runrio.model.data.User;
import tip.edu.ph.runrio.ui.upcoming_race.transaction.racetype.RaceTypeTransactionActivity;

public class UpcomingRaceDetailActivity extends MvpViewStateActivity<UpcomingRaceDetailView, UpcomingRaceDetailPresenter> implements UpcomingRaceDetailView,OnMapReadyCallback {

    private static final String TAG = UpcomingRaceDetailActivity.class.getSimpleName();
    private ActivityUpcomingRaceDetailBinding binding;
    private Realm realm;
    private User user;
    private UpcomingRaces races;
    private ProgressDialog progressDialog;
    private String upcomingID;
    SupportMapFragment mapFragment;
    private RaceKitAdapter racekitAdapter;
    private SponsorAdapter sponsorAdapter;
    private ClaimingRateAdapter claimingPickAdapter;
    private ClaimingRateAdapter claimingDeliveryAdapter;


    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upcoming_race_detail);
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


        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        racekitAdapter = new RaceKitAdapter(this, getMvpView());
        binding.recyclerView.setAdapter(racekitAdapter);

        binding.recyclerView4.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerView4.setItemAnimator(new DefaultItemAnimator());
        sponsorAdapter = new SponsorAdapter(this, getMvpView());
        binding.recyclerView4.setAdapter(sponsorAdapter);

        binding.recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView2.setItemAnimator(new DefaultItemAnimator());
        claimingDeliveryAdapter = new ClaimingRateAdapter(this, getMvpView());
        binding.recyclerView2.setAdapter(claimingDeliveryAdapter);


        binding.recyclerView3.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView3.setItemAnimator(new DefaultItemAnimator());
        claimingPickAdapter = new ClaimingRateAdapter(this, getMvpView());
        binding.recyclerView3.setAdapter(claimingPickAdapter);
    }

    @Override
    public void setUpcomingRaces() {


        races = realm.where(UpcomingRaces.class).findFirst();
        binding.setRace(races);

        racekitAdapter.setUpcomingRaces(realm.copyFromRealm(races.getRacekitallCategory().where()
                .findAll()));
        racekitAdapter.notifyDataSetChanged();

        sponsorAdapter.setUpcomingRaces(realm.copyFromRealm(races.getSponsorsCategory().where()
                .findAll()));
        sponsorAdapter.notifyDataSetChanged();

        claimingDeliveryAdapter.setUpcomingRaces(realm.copyFromRealm(races.getClaimingtypeCategory().where().contains("claimingTypeType","D")
                .findAll()));
        claimingDeliveryAdapter.notifyDataSetChanged();

        claimingPickAdapter.setUpcomingRaces(realm.copyFromRealm(races.getClaimingtypeCategory().where().contains("claimingTypeType","P")
                .findAll()));
        claimingPickAdapter.notifyDataSetChanged();

        String imageURL = Endpoints.IMAGE_URL.replace(Endpoints.IMG_HOLDER, races.getRaceImage());
        Glide.with(UpcomingRaceDetailActivity.this)
                .load(imageURL)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(binding.upcomingDetailImage);

        mapFragment.getMapAsync(this);
        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            binding.upcomingDetailAbout.setText(Html.fromHtml(races.getRaceDescription(),Html.FROM_HTML_MODE_LEGACY));
        } else {
            binding.upcomingDetailAbout.setText(Html.fromHtml(races.getRaceDescription()));
        }*/
    }


    @Override
    public void showSchedules(final UpcomingRaces race) {

        final DialogUpcomingdetailwebviewBinding dialogBinding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.dialog_upcomingdetailwebview, null, false);
        final AlertDialog alert = new AlertDialog.Builder(this)
                .create();
        alert.setCancelable(true);
        alert.setView(dialogBinding.getRoot(),0,0,0,0);
        dialogBinding.webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        dialogBinding.webview.getSettings().setJavaScriptEnabled(true);
        dialogBinding.webview.loadUrl(race.getRaceSchedule());
        dialogBinding.webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(race.getRaceSchedule());
                return true;
            }
        });
        dialogBinding.upcomingDetailDialogTitle.setText(race.getRacesName());
        dialogBinding.upcomingDetailDialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();

    }

    @Override
    public void showRules(final UpcomingRaces race) {
        final DialogUpcomingdetailwebviewBinding dialogBinding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.dialog_upcomingdetailwebview, null, false);
        final AlertDialog alert = new AlertDialog.Builder(this)
                .create();
        alert.setCancelable(true);
        alert.setView(dialogBinding.getRoot(),0,0,0,0);
        dialogBinding.webview.getSettings().setJavaScriptEnabled(true);
        dialogBinding.webview.loadUrl(race.getRaceRules());
        dialogBinding.webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(race.getRaceRules());
                return true;
            }
        });
        dialogBinding.upcomingDetailDialogTitle.setText(race.getRacesName());
        dialogBinding.upcomingDetailDialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    @Override
    public void reserve(final UpcomingRaces race) {
        Intent intent = new Intent(this, RaceTypeTransactionActivity.class);
        intent.putExtra(Constants.UPCOMING_ID, race.getId());
        startActivity(intent);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(Double.parseDouble(races.getRaceLat()), Double.parseDouble(races.getRaceLng()));
        googleMap.addMarker(new MarkerOptions().position(sydney).title(races.getRaceVenue() != null ? races.getRaceVenue() : ""));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(15).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
    public UpcomingRaceDetailPresenter createPresenter() {
        return new UpcomingRaceDetailPresenter();
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
    public ViewState<UpcomingRaceDetailView> createViewState() {
        setRetainInstance(true);
        return new UpcomingRaceDetailViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }
}
