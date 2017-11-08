package tip.edu.ph.runrio.ui.upcoming_race.transaction.claiming;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import tip.edu.ph.runrio.BuildConfig;
import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.app.Constants;
import tip.edu.ph.runrio.app.Endpoints;
import tip.edu.ph.runrio.databinding.ActivityTransactionClaimingBinding;
import tip.edu.ph.runrio.databinding.ActivityUpcomingRaceDetailBinding;
import tip.edu.ph.runrio.databinding.DialogUpcomingdetailwebviewBinding;
import tip.edu.ph.runrio.model.data.UpcomingRaces;
import tip.edu.ph.runrio.model.data.User;

import tip.edu.ph.runrio.ui.upcoming_race.detail.ClaimingRateAdapter;
import tip.edu.ph.runrio.ui.upcoming_race.transaction.expense.ExpenseActivity;
import tip.edu.ph.runrio.ui.upcoming_race.transaction.racetype.RaceTypeTransactionActivity;

public class ClaimingActivity extends MvpViewStateActivity<ClaimingView, ClaimingPresenter> implements ClaimingView {

    private static final String TAG = ClaimingActivity.class.getSimpleName();
    private ActivityTransactionClaimingBinding binding;
    private Realm realm;
    private User user;
    private UpcomingRaces races;
    private ProgressDialog progressDialog;
    private String upcomingID;
    private ClaimingListAdapter claimingPickAdapter;
    SupportMapFragment mapFragment;



    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_claiming);
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

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        claimingPickAdapter = new ClaimingListAdapter(this, getMvpView());
        binding.recyclerView.setAdapter(claimingPickAdapter);

    }


    @Override
    public void showTerms() {
        final DialogUpcomingdetailwebviewBinding dialogBinding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.dialog_upcomingdetailwebview, null, false);
        final AlertDialog alert = new AlertDialog.Builder(this)
                .create();
        alert.setCancelable(true);
        alert.setView(dialogBinding.getRoot(),0,0,0,0);
        dialogBinding.webview.getSettings().setJavaScriptEnabled(true);
        dialogBinding.webview.loadUrl(races.getRaceTerms());
        dialogBinding.webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(races.getRaceTerms());
                return true;
            }
        });
        dialogBinding.upcomingDetailDialogTitle.setText("Terms and Condition");
        dialogBinding.upcomingDetailDialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }


    @Override
    public void showWaiver() {

        final DialogUpcomingdetailwebviewBinding dialogBinding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.dialog_upcomingdetailwebview, null, false);
        final AlertDialog alert = new AlertDialog.Builder(this)
                .create();
        alert.setCancelable(true);
        alert.setView(dialogBinding.getRoot(),0,0,0,0);
        dialogBinding.webview.getSettings().setJavaScriptEnabled(true);
        dialogBinding.webview.loadUrl(races.getRaceWaiver());
        dialogBinding.webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(races.getRaceWaiver());
                return true;
            }
        });
        dialogBinding.upcomingDetailDialogTitle.setText("Waiver");
        dialogBinding.upcomingDetailDialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }


    @Override
    public void reserve() {



        if(binding.checkboxterm.isChecked()&&binding.checkboxwaiver.isChecked())
        {


            Intent intent = new Intent(this, ExpenseActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putString(Constants.CLAIMING_NAME, binding.claimingname.getText().toString());
            mBundle.putString(Constants.CLAIMING_TYPE, "P");
            mBundle.putString(Constants.CLAIMING_TYPE, ""+races.getClaimingtypeCategory().get(0).getId());
            mBundle.putString(Constants.CLAIMING_CONTACT, binding.claimingcontact.getText().toString());
            mBundle.putString(Constants.CLAIMING_EMAIL, binding.claimingemail.getText().toString());
            mBundle.putString(Constants.UPCOMING_ID, races.getId());
            mBundle.putString(Constants.RACE_TYPE_MULTIPLE, getIntent().getStringExtra(Constants.RACE_TYPE_MULTIPLE));
            intent.putExtras(mBundle);
            startActivity(intent);


        }else
        {
            showError("Please read and agree with the waiver and conditions before proceeding");
        }


    }


    @Override
    public void onResume() {
        super.onResume();

        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        races = realm.where(UpcomingRaces.class).findFirst();
        binding.setUser(user);



        claimingPickAdapter.setUpcomingRaces(realm.copyFromRealm(races.getClaimingtypeCategory().where().contains("claimingTypeType","P")
                .findAll()));
        claimingPickAdapter.notifyDataSetChanged();


    }


    @Override
    protected void onDestroy() {
//        races.removeChangeListeners();


        realm.close();
        super.onDestroy();
    }


    @NonNull
    @Override
    public ClaimingPresenter createPresenter() {
        return new ClaimingPresenter();
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
    public ViewState<ClaimingView> createViewState() {
        setRetainInstance(true);
        return new ClaimingViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }
}
