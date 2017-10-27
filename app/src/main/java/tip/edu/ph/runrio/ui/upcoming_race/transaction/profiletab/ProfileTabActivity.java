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
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import tip.edu.ph.runrio.BuildConfig;
import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.app.Constants;
import tip.edu.ph.runrio.databinding.ActivityTransactionProfileBinding;
import tip.edu.ph.runrio.model.data.Profile;
import tip.edu.ph.runrio.model.data.RaceType;
import tip.edu.ph.runrio.model.data.UpcomingRaces;
import tip.edu.ph.runrio.model.data.User;
import tip.edu.ph.runrio.ui.upcoming_race.transaction.profiletab.profilefragment.ProfileInfoFragment;


public class ProfileTabActivity extends AppCompatActivity {

    private Realm realm;
    private User user;
    private ActivityTransactionProfileBinding binding;
    public  ProfileTabAdapter adapter_obj;
    private UpcomingRaces races;
    private List<RaceType> raceTypes;
    private String upcomingID;
    private String singleID="";
    private String multipleID="";

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


    public static ProfileInfoFragment newInstance(int runnerCount, String raceTypeID) {
        ProfileInfoFragment myFragment = new ProfileInfoFragment();

        Bundle args = new Bundle();
        args.putInt(Constants.RUNNER_COUNT, runnerCount);
        args.putString(Constants.RACE_TYPE_ID, raceTypeID);
        myFragment.setArguments(args);

        return myFragment;
    }


    public void initRaceType(Intent mIntent)
    {

        upcomingID = mIntent.getStringExtra(Constants.UPCOMING_ID);
        singleID = mIntent.getStringExtra(Constants.RACE_TYPE_SINGLE);
        multipleID = mIntent.getStringExtra(Constants.RACE_TYPE_MULTIPLE);
        adapter_obj = new ProfileTabAdapter(getSupportFragmentManager());
        adapter_obj.fragmentList = new ArrayList<Fragment>();
        adapter_obj.titleList = new ArrayList<String>();
        races = realm.where(UpcomingRaces.class).findFirst();
        if (upcomingID.equalsIgnoreCase("")) {
            Log.d("TAG", "no int extra found");
            Toast.makeText(this, "Unable to get Event Details", Toast.LENGTH_SHORT).show();
            finish();
        }else if(!(singleID.equalsIgnoreCase("")))
        {
            raceTypes = races.getRacetypeCategory().where().contains("id",singleID).findAll();
            adapter_obj.fragmentList.add(newInstance(1,singleID));
            adapter_obj.titleList.add(raceTypes.get(0).getRaceTypeName());
            adapter_obj.notifyDataSetChanged();
            binding.viewPager.setAdapter(adapter_obj);
            binding.tabs.setupWithViewPager(binding.viewPager, true);
        }
        else if(!(multipleID.equalsIgnoreCase("")))
        {

            raceTypes = races.getRacetypeCategory().where().findAll();
            String[] items = multipleID.split("%");
            int ctr = 0;
            for (String item : items)
            {
                if(!(item.equalsIgnoreCase("0")))
                {
                    adapter_obj.fragmentList.add(newInstance(Integer.parseInt(item), raceTypes.get(ctr).getId()));
                    adapter_obj.titleList.add(raceTypes.get(ctr).getRaceTypeName());
                    adapter_obj.notifyDataSetChanged();
                    binding.viewPager.setAdapter(adapter_obj);
                    binding.tabs.setupWithViewPager(binding.viewPager, true);

                }
                ctr++;
            }
            binding.viewPager.setOffscreenPageLimit(ctr);




        }







    }




}
