package tip.edu.ph.runrio.ui.register;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.databinding.ActivityRegisterBinding;
import tip.edu.ph.runrio.model.data.CityList;
import tip.edu.ph.runrio.model.data.Country;
import tip.edu.ph.runrio.model.data.Province;
import tip.edu.ph.runrio.model.data.SecurityQuestion;
import tip.edu.ph.runrio.model.data.SecurityQuestion2;
import tip.edu.ph.runrio.util.DateTimeUtils;


public class RegisterActivity extends MvpViewStateActivity<RegisterView, RegisterPresenter> implements RegisterView {

    private Realm realm;
    private ActivityRegisterBinding binding;
    private ProgressDialog progressDialog;
    private ArrayList<Integer> countryIdList;
    private ArrayList<Integer> provinceIdList;
    private ArrayList<Integer> cityIdList;
    private ArrayList<String> sq1DescList;
    private ArrayList<String> sq2DescList;
    private ArrayList<String> gender;
    private ArrayList<String> shirtSizes;
    private RealmResults<Country> countryRealmResults;
    private RealmResults<Province> provinceRealmResults;
    private RealmResults<SecurityQuestion> securityQuestionRealmResults;
    private RealmResults<SecurityQuestion2> securityQuestion2RealmResults;
    private String genderStr = "M";


    @SuppressWarnings("ConstantConditions") // for toolbar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        setRetainInstance(true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.setView(getMvpView());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");
        progressDialog.setCancelable(false);

        CheckBox cbPolicy = (CheckBox) findViewById(R.id.cb_policy);
        final CardView btnRegister = (CardView) findViewById(R.id.btn_register);
        TextView tvPolicyTxt = (TextView) findViewById(R.id.tv_policy_text);

        String termsText = "<b>I accept and agree to all of its ";
        String termsLink = getResources().getString(R.string.terms_conditions_link);
        String privacyLink = " and " + getResources().getString(R.string.privacy_link) + "</b>";
        String allText = termsText + termsLink + privacyLink;

        tvPolicyTxt.setMovementMethod(LinkMovementMethod.getInstance());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            tvPolicyTxt.setText(Html.fromHtml(allText, Html.FROM_HTML_MODE_LEGACY));
        else
            tvPolicyTxt.setText(Html.fromHtml(allText));


        /**
         * Enable/Disable the register button when the user did not check the privacy policy
         */
        cbPolicy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    btnRegister.setVisibility(View.VISIBLE);
                else
                    btnRegister.setVisibility(View.INVISIBLE);
            }
        });
        populateGenderAndSizes();
        populateCountry();
        getSQ();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @NonNull
    @Override
    public RegisterPresenter createPresenter() {
        return new RegisterPresenter();
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
    protected void onDestroy() {
        super.onDestroy();
        countryRealmResults.removeChangeListeners();
        provinceRealmResults.removeChangeListeners();
        securityQuestionRealmResults.removeChangeListeners();
        securityQuestion2RealmResults.removeChangeListeners();
        realm.close();
    }

    @Override
    public void showAlert(String message) {
        new AlertDialog.Builder(this)
                .setTitle(message)
                .setPositiveButton("Close", null)
                .show();
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
    public void registerSuccessful(String message) {
        new AlertDialog.Builder(this)
                .setTitle(message)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RegisterActivity.this.finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void onRegister() {
        Map<String, String> fields = new HashMap<String, String>();
        if (binding.spCity.getSelectedItemPosition() == -1) {
            showAlert("Please fill-up all fields");
            return;
        }

        fields.put("email", binding.etEmail.getText().toString());
        fields.put("first_name", binding.etFirstName.getText().toString());
        fields.put("last_name", binding.etLastName.getText().toString());
        fields.put("province_id", provinceIdList.get(binding.spProvince.getSelectedItemPosition()).toString());
        fields.put("city_id", cityIdList.get(binding.spCity.getSelectedItemPosition()).toString());
        fields.put("address", binding.etAddress.getText().toString());
        fields.put("country_id", countryIdList.get(binding.spCountry.getSelectedItemPosition()).toString());
        fields.put("gender", genderStr);
        fields.put("shirt_size", (binding.spShirtsizes.getSelectedItem()).toString());
        fields.put("bday", binding.etBirthday.getText().toString());
        fields.put("contact_prefix", binding.etMobileNumber.getText().toString());
        fields.put("contact_no", binding.etMobileNumber2.getText().toString());
        fields.put("password", binding.etPassword.getText().toString());
        fields.put("password_confirmation", binding.etRepeatPassword.getText().toString());
        fields.put("security_question_1", sq1DescList.get(binding.spSq1.getSelectedItemPosition()));
        fields.put("security_question_ans_1", binding.etSq1Ans.getText().toString());
        fields.put("security_question_2", sq2DescList.get(binding.spSq2.getSelectedItemPosition()));
        fields.put("security_question_ans_2", binding.etSq2Ans.getText().toString());

        presenter.register(fields);

    }

    @Override
    public void onSelectBirthday() {
        Calendar calendar = null;
        if (!binding.etBirthday.getText().toString().isEmpty()) {
            calendar = DateTimeUtils.convertTransactionStringDate(binding.etBirthday.getText().toString(), DateTimeUtils.DATE_ONLY);
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
                binding.etBirthday.setText(dateString);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, listener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    @NonNull
    @Override
    public ViewState<RegisterView> createViewState() {
        setRetainInstance(true);
        return new RegisterViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }

    /**
     * Populate list of province
     */
    private void populateGenderAndSizes() {



        gender = new ArrayList<>();
                gender.add("Male");
                gender.add("Female");


                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.spinner_custom_item, gender);
                binding.spGender.setAdapter(arrayAdapter);


        /**
         * Triggers on click of the spinner
         */
        binding.spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if(binding.spGender.getSelectedItem().toString().equalsIgnoreCase("Male"))
                    genderStr = "M";
                else
                    genderStr = "F";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        shirtSizes = new ArrayList<>();
        shirtSizes.add("X-Small");
        shirtSizes.add("Small");
        shirtSizes.add("Medium");
        shirtSizes.add("Large");
        shirtSizes.add("X-Large");
        shirtSizes.add("XX-Large");


        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(RegisterActivity.this, R.layout.spinner_custom_item, shirtSizes);
        binding.spShirtsizes.setAdapter(arrayAdapter2);


        /**
         * Triggers on click of the spinner
         */
        binding.spShirtsizes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    private void populateCountry() {
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

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.spinner_custom_item, promoList);
                binding.spCountry.setAdapter(arrayAdapter);
                binding.spCountry.setSelection(173);
            }
        });

        /**
         * Triggers on click of the spinner
         */
        binding.spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG>>",binding.spCountry.getSelectedItem().toString());
                if(binding.spCountry.getSelectedItem().toString().equalsIgnoreCase("Philippines"))
                populateProvince();
                else
                {
                    binding.spProvince.setAdapter(null);
                    binding.spCity.setAdapter(null);
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
    private void populateProvince() {
        provinceIdList = new ArrayList<>();
        final List<String> promoList = new ArrayList<>();

        presenter.getProvince();
        provinceRealmResults = realm.where(Province.class).findAll();

        provinceRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Province>>() {
            @Override
            public void onChange(RealmResults<Province> element) {
                provinceIdList.add(0);
                promoList.add("Select Province");
                for (Province province : provinceRealmResults) {
                    promoList.add(province.getName());
                    provinceIdList.add(province.getId());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.spinner_custom_item, promoList);
                binding.spProvince.setAdapter(arrayAdapter);
            }
        });

        /**
         * Triggers on click of the spinner
         */
        binding.spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getCities(provinceIdList.get(position));
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
    private void getCities(Integer province_id) {
        presenter.getCities(province_id);
        RealmResults<CityList> cityRealmResults = realm.where(CityList.class).findAll();
        cityRealmResults.addChangeListener(new RealmChangeListener<RealmResults<CityList>>() {
            @Override
            public void onChange(RealmResults<CityList> element) {
                cityIdList = new ArrayList<>();
                List<String> cityNameList = new ArrayList<>();

                for (CityList cityList : element) {
                    cityNameList.add(cityList.getName());
                    cityIdList.add(cityList.getId());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.spinner_custom_item, cityNameList);
                binding.spCity.setAdapter(null);
                binding.spCity.setAdapter(arrayAdapter);

            }
        });

    }

    /**
     * Get security questions
     */
    private void getSQ() {

        /**
         * Get Security Question set 1
         */
        presenter.getSQ1();
        sq1DescList = new ArrayList<>();

        securityQuestionRealmResults = realm.where(SecurityQuestion.class).findAll();
        securityQuestionRealmResults.addChangeListener(new RealmChangeListener<RealmResults<SecurityQuestion>>() {
            @Override
            public void onChange(RealmResults<SecurityQuestion> element) {
                sq1DescList.add("Select Security Question");
                for (SecurityQuestion securityQuestion : element) {
                    sq1DescList.add(securityQuestion.getDescription());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.spinner_custom_item, sq1DescList);
                binding.spSq1.setAdapter(arrayAdapter);
            }
        });

        /**
         * Get Security Question Set 2
         */
        presenter.getSQ2();
        sq2DescList = new ArrayList<>();

        securityQuestion2RealmResults = realm.where(SecurityQuestion2.class).findAll();
        securityQuestion2RealmResults.addChangeListener(new RealmChangeListener<RealmResults<SecurityQuestion2>>() {
            @Override
            public void onChange(RealmResults<SecurityQuestion2> element) {
                sq2DescList.add("Select Security Question");
                for (SecurityQuestion2 securityQuestion : element) {
                    sq2DescList.add(securityQuestion.getDescription());
                }
                ArrayAdapter<String> sQ2arrayAdapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.spinner_custom_item, sq2DescList);
                binding.spSq2.setAdapter(sQ2arrayAdapter);
            }
        });

    }


}
