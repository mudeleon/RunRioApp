package tip.edu.ph.runrio.ui.upcoming_race.transaction.profiletab;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import tip.edu.ph.runrio.BuildConfig;
import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.app.Constants;
import tip.edu.ph.runrio.databinding.ActivityTransactionProfileBinding;
import tip.edu.ph.runrio.model.data.UpcomingRaces;
import tip.edu.ph.runrio.model.data.User;
import tip.edu.ph.runrio.ui.upcoming_race.transaction.profiletab.profilefragment.ProfileInfoFragment;


public class ProfileTabActivity extends AppCompatActivity {

    private Realm realm;
    private User user;
    private ActivityTransactionProfileBinding binding;
    public static ProfileTabAdapter adapter_obj;
    private UpcomingRaces races;
    private String upcomingID;
    private String singleID;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();


        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_profile);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(BuildConfig.DEBUG ? "Upcoming Races" : "Upcoming Races");


        initRaceType(getIntent());

    }


    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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


    public static ProfileInfoFragment newInstance(int someInt) {
        ProfileInfoFragment myFragment = new ProfileInfoFragment();

        Bundle args = new Bundle();
        args.putInt("someInt", someInt);
        myFragment.setArguments(args);

        return myFragment;
    }


    public void initRaceType(Intent mIntent)
    {

        upcomingID = getIntent().getStringExtra(Constants.UPCOMING_ID);
        singleID = getIntent().getStringExtra(Constants.RACE_TYPE_SINGLE);
        if (upcomingID.equalsIgnoreCase("")) {
            Log.d("TAG", "no int extra found");
            Toast.makeText(this, "Unable to get Event Details", Toast.LENGTH_SHORT).show();
            finish();
        }else if(upcomingID.equalsIgnoreCase(""))
        {
            races = realm.where(UpcomingRaces.class).findFirst();
        }


        adapter_obj = new ProfileTabAdapter(getSupportFragmentManager());
        adapter_obj.fragmentList = new ArrayList<Fragment>();
        adapter_obj.fragmentList.add(newInstance(1));
        adapter_obj.titleList.add("");
        adapter_obj.notifyDataSetChanged();
        binding.viewPager.setAdapter(adapter_obj);
        binding.tabs.setupWithViewPager(binding.viewPager, true);

    }

}
