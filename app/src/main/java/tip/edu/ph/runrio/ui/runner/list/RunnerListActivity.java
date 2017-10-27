package tip.edu.ph.runrio.ui.runner.list;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;
import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.databinding.ActivityRunnerListBinding;
import tip.edu.ph.runrio.databinding.DialogEditRunnerBinding;
import tip.edu.ph.runrio.databinding.DialogRunnerProfileBinding;
import tip.edu.ph.runrio.model.data.CityList;
import tip.edu.ph.runrio.model.data.Country;
import tip.edu.ph.runrio.model.data.Profile;
import tip.edu.ph.runrio.model.data.Province;
import tip.edu.ph.runrio.model.data.User;
import tip.edu.ph.runrio.ui.profile.edit.EditProfileActivity;
import tip.edu.ph.runrio.ui.register.RegisterActivity;
import tip.edu.ph.runrio.util.CircleTransform;
import tip.edu.ph.runrio.util.DateTimeUtils;


public class RunnerListActivity extends MvpViewStateActivity<RunnerListView, RunnerListPresenter>
        implements RunnerListView, SwipeRefreshLayout.OnRefreshListener {

    private ActivityRunnerListBinding binding;
    private Realm realm;
    private User user;
    private RunnerListAdapter adapterRunnerList;
    private RealmResults<Profile> profileRealmResults;
     DialogRunnerProfileBinding dialogBinding;
    DialogEditRunnerBinding dialogBindingEdit;
    private ArrayList<Integer> provinceIdList;
    private ArrayList<Integer> cityIdList;
    private ArrayList<Integer> countryIdList;
    private RealmResults<Country> countryRealmResults;
    private RealmResults<CityList> cityRealmResults;
    private RealmResults<Country> countryRealmResults2;
    private ProgressDialog progressDialog;

    private ArrayList<String> gender;
    private String genderStr = "M";
    private boolean addOrEdit = true;

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

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");
        progressDialog.setCancelable(false);
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
        getMenuInflater().inflate(R.menu.menu_runner, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_add_runner:
                addRunner();
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
        profileRealmResults = realm.where(Profile.class).contains("profileActive","Y").findAllSorted("id");
            if (!(profileRealmResults.isEmpty())) {
                getMvpView().setRunnerList();

            }else
            {
                presenter.loadRunner(user.getApiToken());
            }
    }


    @Override
    public void setRunnerList() {
        profileRealmResults = realm.where(Profile.class).findAllAsync();
        adapterRunnerList.setRunnerList(realm.copyToRealmOrUpdate( profileRealmResults.where().contains("profileActive","Y").findAllSorted("id")));
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

        dialogBinding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.dialog_runner_profile, null, false);
        final AlertDialog alert = new AlertDialog.Builder(this)
                .create();
        alert.setCancelable(true);
        alert.setView(dialogBinding.getRoot(),0,0,0,0);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogBinding.setProfile(runner);
        dialogBinding.setView(getMvpView());
        int pictureSwitcher;
       if(runner.getProfileGender().equalsIgnoreCase("M"))
           pictureSwitcher = R.drawable.ic_runner_profile_m;
        else
           pictureSwitcher = R.drawable.ic_runner_profile_f;
        Glide.with(this)
                .load(pictureSwitcher)
                .transform(new CircleTransform(this))
                .into(dialogBinding.imageRunnerProfile);
        dialogBinding.runnerProfileClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();

    }

    @Override
    public void onSaveAdd(Profile runner) {

        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("email", dialogBindingEdit.etEmail.getText().toString());
        fields.put("first_name", dialogBindingEdit.etFirstName.getText().toString());
        fields.put("last_name", dialogBindingEdit.etLastName.getText().toString());
        fields.put("province_id", provinceIdList.get(dialogBindingEdit.spProvince.getSelectedItemPosition()).toString());
        fields.put("city_id", cityIdList.get(dialogBindingEdit.spCity.getSelectedItemPosition()).toString());
        fields.put("country_id", countryIdList.get(dialogBindingEdit.spCountry.getSelectedItemPosition()).toString());
        fields.put("gender", genderStr);
        //fields.put("shirt_size", (binding.spShirtsizes.getSelectedItem()).toString());
        fields.put("bday", dialogBindingEdit.etBirthday.getText().toString());
        fields.put("contact_prefix", dialogBindingEdit.etMobileNumber.getText().toString());
        fields.put("contact_no", dialogBindingEdit.etMobileNumber2.getText().toString());


        if(addOrEdit)
        {

            presenter.editRunner(fields,String.valueOf(runner.getId()),user.getApiToken());
        }else
        {

            presenter.registerRunner(fields,user.getApiToken());
        }

    }

    @Override
    public void onSuccess() {

    }
    @Override
    public void onEditProfile(Profile runner) {

        dialogBindingEdit = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.dialog_edit_runner, null, false);


        final AlertDialog alert = new AlertDialog.Builder(this)
                .create();
        alert.setCancelable(true);
        alert.setView(dialogBindingEdit.getRoot(),0,0,0,0);
        dialogBindingEdit.setUser(runner);
        dialogBindingEdit.setView(getMvpView());
        populateCountry(runner);
        populateGenderAndSizes(runner);
        addOrEdit = true;
        alert.show();



    }

    @Override
    public void onDeleteProfile(Profile runner) {

        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();

        presenter.deleteRunner(String.valueOf(runner.getId()),user.getApiToken());

    }


    public void addRunner()
    {
        dialogBindingEdit = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.dialog_edit_runner, null, false);


        final AlertDialog alert = new AlertDialog.Builder(this)
                .create();
        alert.setCancelable(true);
        alert.setView(dialogBindingEdit.getRoot(),0,0,0,0);
        dialogBindingEdit.setView(getMvpView());
        populateCountry2();
        populateGenderAndSizes2();
         addOrEdit = false;
        alert.show();
    }

    @Override
    public void onRefresh() {

        presenter.loadRunner(user.getApiToken());


    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void dismissProgress() {
        progressDialog.dismiss();
    }


    @Override
    public void onSelectBirthday() {


        Calendar calendar = null;
        if (!dialogBindingEdit.etBirthday.getText().toString().isEmpty()) {
            calendar = DateTimeUtils.convertTransactionStringDate(dialogBindingEdit.etBirthday.getText().toString(), DateTimeUtils.DATE_ONLY);
        }
        if (calendar == null) calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, i);
                calendar1.set(Calendar.MONTH, i1);
                calendar1.set(Calendar.DAY_OF_MONTH, i2);
                String dateString = DateTimeUtils.convertDateToString(DateTimeUtils.DATE_ONLY, calendar1);
                dialogBindingEdit.etBirthday.setText(dateString);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, listener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    /**
     * Populate list of province
     */
    private void populateProvince(final Profile runner) {
        provinceIdList = new ArrayList<>();
        final List<String> promoList = new ArrayList<>();

        presenter.getProvince();
       final RealmResults<Province> provinceRealmResults = realm.where(Province.class).findAll();
        provinceRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Province>>() {
            @Override
            public void onChange(RealmResults<Province> element) {
                for (Province province : provinceRealmResults) {
                    promoList.add(province.getName());
                    provinceIdList.add(province.getId());
                }
                Log.d(">>>>>>>","SHITPOPULATE1");
                ArrayAdapter arrayAdapter = new ArrayAdapter<>(RunnerListActivity.this, R.layout.spinner_custom_item, promoList);
                dialogBindingEdit.spProvince.setAdapter(null);
                dialogBindingEdit.spProvince.setAdapter(arrayAdapter);
                dialogBindingEdit.spProvince.setSelection(provinceIdList.indexOf(runner.getCity().getProvince_id()));
            }
        });


        /**
         * Triggers on click of the spinner
         */
        dialogBindingEdit.spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getCities(provinceIdList.get(position),runner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Get the cities by province id
     *
     * @param province_id - the id of province
     */
    private void getCities(Integer province_id,final Profile runner ) {
        presenter.getCities(province_id);
        cityRealmResults = realm.where(CityList.class).findAll();
        cityRealmResults.addChangeListener(new RealmChangeListener<RealmResults<CityList>>() {
            @Override
            public void onChange(RealmResults<CityList> element) {
                cityIdList = new ArrayList<>();
                List<String> cityNameList = new ArrayList<>();

                for (CityList cityList : element) {
                    cityNameList.add(cityList.getName());
                    cityIdList.add(cityList.getId());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RunnerListActivity.this, R.layout.spinner_custom_item, cityNameList);
                dialogBindingEdit.spCity.setAdapter(null);
                dialogBindingEdit.spCity.setAdapter(arrayAdapter);
                dialogBindingEdit.spCity.setSelection(cityIdList.indexOf(runner.getCity().getId()));

            }
        });

    }

    private void populateGenderAndSizes(Profile runner) {

        gender = new ArrayList<>();

        if(runner.getProfileGender().equalsIgnoreCase("M")) {
            gender.add("Male");
            gender.add("Female");
        }else {
            gender.add("Female");
            gender.add("Male");
        }


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RunnerListActivity.this, R.layout.spinner_custom_item, gender);
        dialogBindingEdit.spGender.setAdapter(arrayAdapter);


        /**
         * Triggers on click of the spinner
         */
        dialogBindingEdit.spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if(dialogBindingEdit.spGender.getSelectedItem().toString().equalsIgnoreCase("Male"))
                    genderStr = "M";
                else
                    genderStr = "F";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }


    private void populateCountry(final Profile runner) {
        countryIdList = new ArrayList<>();
        final List<String> promoList = new ArrayList<>();

        presenter.getCountry();

        countryRealmResults = realm.where(Country.class).findAll();

        countryRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Country>>() {
            @Override
            public void onChange(RealmResults<Country> element) {
                countryIdList.add(0);
                promoList.add("Select Country");
                for (Country province : countryRealmResults) {
                    promoList.add(province.getName());
                    countryIdList.add(province.getId());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RunnerListActivity.this, R.layout.spinner_custom_item, promoList);
                dialogBindingEdit.spCountry.setAdapter(arrayAdapter);
                if(runner.getProfileCountry().equalsIgnoreCase("Philippines"))
                dialogBindingEdit.spCountry.setSelection(173);
            }
        });

        /**
         * Triggers on click of the spinner
         */
        dialogBindingEdit.spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(dialogBindingEdit.spCountry.getSelectedItem().toString().equalsIgnoreCase("Philippines"))
                    populateProvince(runner);
                else
                {
                    dialogBindingEdit.spProvince.setAdapter(null);
                    dialogBindingEdit.spCity.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }



    //new runner functions

    private void populateCountry2() {
        countryIdList = new ArrayList<>();
        final List<String> promoList = new ArrayList<>();

        presenter.getCountry();
        countryRealmResults2 = realm.where(Country.class).findAll();

        countryRealmResults2.addChangeListener(new RealmChangeListener<RealmResults<Country>>() {
            @Override
            public void onChange(RealmResults<Country> element) {
                countryIdList.add(0);
                promoList.add("Select Country");
                for (Country province : countryRealmResults2) {
                    promoList.add(province.getName());
                    countryIdList.add(province.getId());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RunnerListActivity.this, R.layout.spinner_custom_item, promoList);
                dialogBindingEdit.spCountry.setAdapter(arrayAdapter);
                dialogBindingEdit.spCountry.setSelection(173);
            }
        });

        /**
         * Triggers on click of the spinner
         */
        dialogBindingEdit.spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG2>>",dialogBindingEdit.spCountry.getSelectedItem().toString());
                if(dialogBindingEdit.spCountry.getSelectedItem().toString().equalsIgnoreCase("Philippines"))
                    populateProvince2();
                else
                {
                    dialogBindingEdit.spProvince.setAdapter(null);
                    dialogBindingEdit.spCity.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }



    /**
     * Populate list of province
     */
    private void populateProvince2() {
        provinceIdList = new ArrayList<>();
        final List<String> promoList = new ArrayList<>();

        presenter.getProvince();
        final RealmResults<Province> provinceRealmResults2 = realm.where(Province.class).findAll();

        provinceRealmResults2.addChangeListener(new RealmChangeListener<RealmResults<Province>>() {
            @Override
            public void onChange(RealmResults<Province> element) {
                for (Province province : provinceRealmResults2) {
                    promoList.add(province.getName());
                    provinceIdList.add(province.getId());
                }
                Log.d(">>>>>>>","SHITPOPULATE2");
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RunnerListActivity.this, R.layout.spinner_custom_item, promoList);
                dialogBindingEdit.spProvince.setAdapter(arrayAdapter);
            }
        });

        /**
         * Triggers on click of the spinner
         */
        dialogBindingEdit.spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getCities2(provinceIdList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    /**
     * Get the cities by province id
     *
     * @param province_id - the id of province
     */
    private void getCities2(Integer province_id) {
        presenter.getCities(province_id);
         cityRealmResults = realm.where(CityList.class).findAll();
        cityRealmResults.addChangeListener(new RealmChangeListener<RealmResults<CityList>>() {
            @Override
            public void onChange(RealmResults<CityList> element) {
                cityIdList = new ArrayList<>();
                List<String> cityNameList = new ArrayList<>();

                for (CityList cityList : element) {
                    cityNameList.add(cityList.getName());
                    cityIdList.add(cityList.getId());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RunnerListActivity.this, R.layout.spinner_custom_item, cityNameList);
                dialogBindingEdit.spCity.setAdapter(null);
                dialogBindingEdit.spCity.setAdapter(arrayAdapter);

            }
        });

    }
    private void populateGenderAndSizes2() {


        gender = new ArrayList<>();
        gender.add("Male");
        gender.add("Female");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RunnerListActivity.this, R.layout.spinner_custom_item, gender);
        dialogBindingEdit.spGender.setAdapter(arrayAdapter);
        /**
         * Triggers on click of the spinner
         */
        dialogBindingEdit.spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(dialogBindingEdit.spGender.getSelectedItem().toString().equalsIgnoreCase("Male"))
                genderStr = "M";
            else
                genderStr = "F";
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });


}


}
