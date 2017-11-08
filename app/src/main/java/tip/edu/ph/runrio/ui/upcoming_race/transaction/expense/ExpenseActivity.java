package tip.edu.ph.runrio.ui.upcoming_race.transaction.expense;

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

import com.google.android.gms.maps.SupportMapFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
import tip.edu.ph.runrio.BuildConfig;
import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.app.Constants;
import tip.edu.ph.runrio.databinding.ActivityTransactionBreakdownBinding;
import tip.edu.ph.runrio.databinding.ActivityTransactionClaimingBinding;
import tip.edu.ph.runrio.databinding.DialogUpcomingdetailwebviewBinding;
import tip.edu.ph.runrio.model.data.RaceType;
import tip.edu.ph.runrio.model.data.TransactionData;
import tip.edu.ph.runrio.model.data.UpcomingRaces;
import tip.edu.ph.runrio.model.data.User;
import tip.edu.ph.runrio.ui.main.RacesListActivity;
import tip.edu.ph.runrio.ui.upcoming_race.detail.ClaimingRateAdapter;

public class ExpenseActivity extends MvpViewStateActivity<ExpenseView, ExpensePresenter> implements ExpenseView {

    private static final String TAG = ExpenseActivity.class.getSimpleName();
    private ActivityTransactionBreakdownBinding binding;
    private Realm realm;
    private User user;
    private UpcomingRaces races;
    private ProgressDialog progressDialog;
    private String upcomingID;
    private List<String> runner;
    private List<String> raceType;
    private List<String> price;
    private double totalPrice=0;
    private ExpenseListAdapter expenseAdapter;
    private RealmResults<TransactionData> savedData;




    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_breakdown);
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
        expenseAdapter = new ExpenseListAdapter(this, getMvpView());
        binding.recyclerView.setAdapter(expenseAdapter);


    }


    @Override
    public void reserve() {






        savedData = realm.where(TransactionData.class).findAll();
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("event_id", races.getId());
        fields.put("account_id", String.valueOf(user.getUserID()));
        fields.put("claim_type", getIntent().getStringExtra(Constants.CLAIMING_TYPE));
        fields.put("claim_name",   getIntent().getStringExtra(Constants.CLAIMING_NAME));
        fields.put("claim_email", getIntent().getStringExtra(Constants.CLAIMING_EMAIL));
        fields.put("claim_contact", getIntent().getStringExtra(Constants.CLAIMING_CONTACT));
        fields.put("runner_count", String.valueOf(savedData.size()));


        for(int a=0; a<savedData.size();a++)
        {
           // Log.d(">>>",  "ID>>"+savedData.get(a).getId()+ "   ProfileID>>"+savedData.get(a).getTransactionDataProfileId());
            fields.put("profile_id"+a, savedData.get(a).getTransactionDataProfileId());

            for(int b=0; b<1/*savedData.get(a).getTransactionDataRaceKit().size()*/;b++)
            {
               // Log.d(">>>",  "IDRaceKit>>"+savedData.get(a).getTransactionDataRaceKit().get(b).getId()+"    RaceKit>>"+savedData.get(a).getTransactionDataRaceKit().get(b).getVal());
                fields.put("racekit"+a, savedData.get(a).getTransactionDataRaceKit().get(b).getVal());
            }
            for(int b=0; b<1/*savedData.get(a).getTransactionDataRaceKitChoices().size()*/;b++)
            {
                //Log.d(">>>",  "IDKitChoices>>"+savedData.get(a).getTransactionDataRaceKitChoices().get(b).getId()+"    Choices>>"+savedData.get(a).getTransactionDataRaceKitChoices().get(b).getVal());
                fields.put("choice"+a, savedData.get(a).getTransactionDataRaceKitChoices().get(b).getVal());
            }
        }

          //  presenter.registerRunner(fields,user.getApiToken());


        showError("Payment Module Still on Progress!");
    }


    @Override
    public void onReserveSuccess() {



        showError("Sucessfully Reserved!");
        Intent intent = new Intent(this,RacesListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }


    @Override
    public void onResume() {
        super.onResume();

        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        races = realm.where(UpcomingRaces.class).findFirst();

        raceType = new ArrayList<>();
        runner = new ArrayList<>();
        price = new ArrayList<>();

        String multipleID = getIntent().getStringExtra(Constants.RACE_TYPE_MULTIPLE);


        if(!(multipleID.contains("%")))
        {
           RaceType raceTypes = races.getRacetypeCategory().where().contains("id",multipleID).findFirst();
            raceType.add(raceTypes.getRaceTypeName());
            runner.add("1");
            price.add(raceTypes.getRaceTypeLocFee());
            totalPrice = Double.parseDouble(raceTypes.getRaceTypeLocFee());

        }else {

            String[] items = multipleID.split("%");
            int ctr = 0;
            double temp = 0;
            for (String item : items) {
                if (!(item.equalsIgnoreCase("0"))) {
                    raceType.add(races.getRacetypeCategory().get(ctr).getRaceTypeName());
                    runner.add(item);
                    temp =  (Integer.parseInt(races.getRacetypeCategory().get(ctr).getRaceTypeLocFee())) * (Integer.parseInt(item));
                    price.add(String.valueOf(temp));

                    totalPrice = totalPrice + temp;

                }
                ctr++;
            }
        }

        expenseAdapter.setBreakList(raceType,runner,price);
        expenseAdapter.notifyDataSetChanged();
        binding.breakdownTotalPrice.setText(String.valueOf(totalPrice));







    }


    @Override
    protected void onDestroy() {
//        races.removeChangeListeners();


        realm.close();
        super.onDestroy();
    }


    @NonNull
    @Override
    public ExpensePresenter createPresenter() {
        return new ExpensePresenter();
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
    public ViewState<ExpenseView> createViewState() {
        setRetainInstance(true);
        return new ExpenseViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void dismissProgress() {
        progressDialog.dismiss();
    }

}
