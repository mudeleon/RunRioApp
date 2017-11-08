package tip.edu.ph.runrio.ui.upcoming_race.transaction.profiletab.profilefragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

import io.realm.RealmResults;
import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.app.Constants;

import tip.edu.ph.runrio.databinding.ActivityTransactionRunnerListBinding;
import tip.edu.ph.runrio.databinding.DialogListRunnerBinding;
import tip.edu.ph.runrio.model.data.Profile;
import tip.edu.ph.runrio.model.data.TransactionData;
import tip.edu.ph.runrio.model.data.UpcomingRaces;
import tip.edu.ph.runrio.model.data.User;
import tip.edu.ph.runrio.ui.upcoming_race.transaction.claiming.ClaimingActivity;
import tip.edu.ph.runrio.ui.upcoming_race.transaction.profiletab.ProfileTabActivity;


public class ProfileInfoFragment extends MvpViewStateFragment<ProfileView, ProfilePresenter>
        implements ProfileView{


    private ActivityTransactionRunnerListBinding binding;
    private RealmResults<TransactionData> transactionRealmResults;
    private Realm realm;
    private UpcomingRaces races;
    private RealmResults<TransactionData> savedData;
    private RealmResults<Profile> profileRealmResults;
    User user;
    private List<Profile> profileList = new ArrayList<>();
    private List<String> raceKit = new ArrayList<>();
    private List<String> raceChoices = new ArrayList<>();
    private ProfileListAdapter adapterRunnerList;
    private ProfileDialogListAdapter adapterRunnerDialog;
    private ProgressDialog progressDialog;
    private String positiontoFill; int positiontoFill2;
    private AlertDialog alert;


    public ProfileInfoFragment() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @NonNull
    @Override
    public ProfilePresenter createPresenter() {
        return new ProfilePresenter();
    }

    @NonNull
    @Override
    public ViewState createViewState() {
        setRetainInstance(true);
        return new ProfileViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_transaction_runner_list, container, false);
        binding.setView(getMvpView());

        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        //binding.setView(getMvpView());


        adapterRunnerList = new ProfileListAdapter(getContext(), getMvpView());
        binding.recyclerView.setAdapter(adapterRunnerList);
        getArguments().getString(Constants.RACE_TYPE_ID);
        for(int a=0;a<getArguments().getInt(Constants.RUNNER_COUNT, 0);a++) {
            Profile sampler = new Profile();
            sampler.setNullCheckerTransactionList(String.valueOf(a));
            profileList.add(sampler);
        }
        popolateKitChoices();
        //popolateTransactionData(getArguments().getInt(Constants.RUNNER_COUNT, 0));
        adapterRunnerList.setRunnerList(profileList,raceKit,raceChoices,getArguments().getInt(Constants.CONTROL_NUMBER,0));
        adapterRunnerList.notifyDataSetChanged();


        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();






        //binding.setView(getMvpView());

    }

    @Override
    public void onStop() {


        realm.close();
        super.onStop();
    }


    @Override
    public void onSaveChoices() {

        Log.d(">>>",">>>");

        savedData = realm.where(TransactionData.class).findAll();
        for(int a=0; a<savedData.size();a++)
        {
            Log.d(">>>",  "ID>>"+savedData.get(a).getId()+ "   ProfileID>>"+savedData.get(a).getTransactionDataProfileId());

            for(int b=0; b<savedData.get(a).getTransactionDataRaceKit().size();b++)
            {
                Log.d(">>>",  "IDRaceKit>>"+savedData.get(a).getTransactionDataRaceKit().get(b).getId()+"    RaceKit>>"+savedData.get(a).getTransactionDataRaceKit().get(b).getVal());

            }
            for(int b=0; b<savedData.get(a).getTransactionDataRaceKitChoices().size();b++)
            {
                Log.d(">>>",  "IDKitChoices>>"+savedData.get(a).getTransactionDataRaceKitChoices().get(b).getId()+"    Choices>>"+savedData.get(a).getTransactionDataRaceKitChoices().get(b).getVal());

            }
        }

        Intent intent = new Intent(getActivity(), ClaimingActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString(Constants.UPCOMING_ID, races.getId());
        mBundle.putString(Constants.RACE_TYPE_MULTIPLE,getArguments().getString(Constants.RACE_TYPE_MULTIPLE) );
        intent.putExtras(mBundle);
        startActivity(intent);

    }


    @Override
    public void onAddRunner(Profile runner) {


      DialogListRunnerBinding dialogBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater(),
                R.layout.dialog_list_runner, null, false);
         alert = new AlertDialog.Builder(getContext())
                .create();
        alert.setCancelable(true);
        alert.setView(dialogBinding.getRoot(),0,0,0,0);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogBinding.setProfile(runner);
        dialogBinding.setView(getMvpView());

        dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dialogBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        //binding.setView(getMvpView());


        adapterRunnerDialog = new ProfileDialogListAdapter(getContext(), getMvpView());
        dialogBinding.recyclerView.setAdapter(adapterRunnerDialog);

        profileRealmResults = realm.where(Profile.class).contains("profileActive","Y").findAllSorted("id");
        adapterRunnerDialog.setRunnerList(profileRealmResults);
        adapterRunnerDialog.notifyDataSetChanged();

        dialogBinding.runnerProfileClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();


        if(runner.getNullCheckerTransactionList()!=null) {
            positiontoFill = runner.getNullCheckerTransactionList();
            positiontoFill2 = -1;
        }
        else
        {
            positiontoFill = "-1";
            positiontoFill2 = runner.getId();
        }






    }

    @Override
    public void onDialogRunnerClicked (Profile runner) {

        if(duplicateChecker(runner)){
            for(int a=0;a<getArguments().getInt(Constants.RUNNER_COUNT, 0);a++) {
                Profile sampler = new Profile();
                sampler.setNullCheckerTransactionList(String.valueOf(a));
                if (profileList.get(a).getNullCheckerTransactionList() != null) {
                    if (a == Integer.parseInt(positiontoFill)) {
                        profileList.set(a, runner);
                        final TransactionData data = realm.where(TransactionData.class)
                                .equalTo("id", a+(getArguments().getInt(Constants.CONTROL_NUMBER,0)))
                                .findFirst();
                        realm.beginTransaction();
                        data.setTransactionDataProfileId(String.valueOf(runner.getId()));
                        realm.commitTransaction();
                    }

                } else if (profileList.get(a).getId() == positiontoFill2) {
                    profileList.set(a, runner);
                    final TransactionData data = realm.where(TransactionData.class)
                            .equalTo("id", a+(getArguments().getInt(Constants.CONTROL_NUMBER,0)))
                            .findFirst();
                    realm.beginTransaction();
                    data.setTransactionDataProfileId(String.valueOf(runner.getId()));
                    realm.commitTransaction();
                }
            }
        }else
            showToast("Runner Already Assigned!");
        alert.dismiss();
        popolateKitChoices();
        adapterRunnerList.setRunnerList(profileList,raceKit,raceChoices,getArguments().getInt(Constants.CONTROL_NUMBER,0));
        adapterRunnerList.notifyDataSetChanged();

    }



        @Override
    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Connecting");
        }
        progressDialog.show();
    }

    @Override
    public void stopProgress() {
        if (progressDialog != null) progressDialog.dismiss();

    }
    @Override
    public void showAlert(String message) {

            new AlertDialog.Builder(getActivity())
                    .setTitle(message)
                    .setPositiveButton( "Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();

    }


    public void showToast(String message) {
        Toast.makeText(getContext(), message,
                Toast.LENGTH_SHORT).show();

    }

    public boolean duplicateChecker(Profile runner)
    {
        boolean duplicateChecker = true;
        for(int a=0;a<getArguments().getInt(Constants.RUNNER_COUNT, 0);a++) {

            if(profileList.get(a).getNullCheckerTransactionList()==null)
            {
                if(profileList.get(a).getId() == runner.getId())
                    duplicateChecker = false;
            }
        }

        return duplicateChecker;
    }


    public void popolateKitChoices()
    {

        String tempString="";
        races = realm.where(UpcomingRaces.class).findFirst();
        for(int a=0;a<races.getRacetypeCategory().size();a++) {
            if((races.getRacetypeCategory().get(a).getId()).equalsIgnoreCase(getArguments().getString(Constants.RACE_TYPE_ID))) {
              //  Log.d(">>RaceType >", races.getRacetypeCategory().get(a).getRaceTypeName());
                for(int b=0;b<races.getRacetypeCategory().get(a).getRacekitCategory().size();b++) {
                    if(races.getRacetypeCategory().get(a).getRacekitCategory().get(b).getRaceKitChoicesCategory().isEmpty()) {
                  //      Log.d(">>No choices >", races.getRacetypeCategory().get(a).getRacekitCategory().get(b).getRaceKitName());
                    }
                    else
                    {
                        raceKit.add(races.getRacetypeCategory().get(a).getRacekitCategory().get(b).getRaceKitName());
                    //    Log.d(">>with  choices >", races.getRacetypeCategory().get(a).getRacekitCategory().get(b).getRaceKitName());
                        for(int c=0;c<races.getRacetypeCategory().get(a).getRacekitCategory().get(b).getRaceKitChoicesCategory().size();c++) {
                    //        Log.d(">>choices >", races.getRacetypeCategory().get(a).getRacekitCategory().get(b).getRaceKitChoicesCategory().get(c).getRaceKitChoicesChoice());
                          tempString += races.getRacetypeCategory().get(a).getRacekitCategory().get(b).getRaceKitChoicesCategory().get(c).getRaceKitChoicesChoice()+"%";
                        }
                        raceChoices.add(tempString);
                        tempString="";
                    }

                }
            }
        }

    }






}
