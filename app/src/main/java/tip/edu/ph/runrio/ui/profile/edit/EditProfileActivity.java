package tip.edu.ph.runrio.ui.profile.edit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmResults;
import pl.aprilapps.easyphotopicker.EasyImage;
import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.app.Endpoints;
import tip.edu.ph.runrio.databinding.ActivityEditProfileBinding;
import tip.edu.ph.runrio.model.data.CityList;
import tip.edu.ph.runrio.model.data.Province;
import tip.edu.ph.runrio.model.data.User;
import tip.edu.ph.runrio.util.CircleTransform;
import tip.edu.ph.runrio.util.DateTimeUtils;


public class EditProfileActivity extends MvpViewStateActivity<EditProfileView, EditProfilePresenter>
        implements EditProfileView {

    private static final int PERMISSION_READ_EXTERNAL_STORAGE = 124;
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 125;
    private static final int PERMISSION_CAMERA = 126;
    private static final String TAG = EditProfileActivity.class.getSimpleName();

    private ActivityEditProfileBinding binding;
    private Realm realm;
    private ProgressDialog progressDialog;
    private User user;
    private ArrayList<Integer> provinceIdList;
    private ArrayList<Integer> cityIdList;
    private RealmResults<Province> provinceRealmResults;
    private RealmResults<CityList> cityRealmResults;
    private boolean isFirstTimeCities = true;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        EasyImage.configuration(this)
                .setImagesFolderName("SchedulerApp")
                .saveInRootPicturesDirectory();
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit My Profile");
        binding.setActivity(this);
        binding.setUser(user);
        loadImage();
        user.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                loadImage();
            }
        });

    }


    private void loadImage() {
        String imageURL = Endpoints.IMAGE_URL.replace(Endpoints.IMG_HOLDER, user.getImage());
        if (user.getFbID() != null && !user.getFbID().isEmpty()) {
            imageURL = Endpoints.IMAGE_URL_FB.replace(Endpoints.IMG_HOLDER, user.getFbID());
        }
        Glide.with(this)
                .load(imageURL)
                .transform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imageView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        populateProvince();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        user.removeChangeListeners();
        provinceRealmResults.removeChangeListeners();
        cityRealmResults.removeChangeListeners();
        realm.close();
        super.onDestroy();
    }

    @NonNull
    @Override
    public EditProfilePresenter createPresenter() {
        return new EditProfilePresenter();
    }

    @NonNull
    @Override
    public ViewState<EditProfileView> createViewState() {
        setRetainInstance(true);
        return new EditProfileViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }

    public void onSelectBirthDay() {
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

    public void onSaveChanges() {
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("first_name", binding.etFirstName.getText().toString());
        fields.put("last_name", binding.etLastName.getText().toString());
        fields.put("province_id", provinceIdList.get(binding.spProvince.getSelectedItemPosition()).toString());
        fields.put("city_id", cityIdList.get(binding.spCity.getSelectedItemPosition()).toString());
        fields.put("address", binding.etAddress.getText().toString());
        fields.put("bday", binding.etBirthday.getText().toString());
        fields.put("cp_number", binding.etMobileNumber.getText().toString());

//        presenter.updateProfile(user.getApiToken(), binding.etFirstName.getText().toString(), binding.etLastName.getText().toString(),
//                binding.etAddress.getText().toString(), binding.etBirthday.getText().toString(),
//                binding.etMobileNumber.getText().toString());
        presenter.updateProfile(user.getApiToken(), fields);
    }

    @Override
    public void showAlert(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Connecting...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    @Override
    public void updateAccountSuccessful(String message) {
        new AlertDialog.Builder(this)
                .setTitle(message)
                .setCancelable(false)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditProfileActivity.this.finish();
                    }
                })
                .show();
    }

    public void onChangeUserImage() {
        if (user.getFbID() != null && !user.getFbID().isEmpty()) {
            return;
        }
        PopupMenu popupMenu = new PopupMenu(this, binding.btnChangeImage);
        popupMenu.inflate(R.menu.edit_user_image);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_select_picture:
                        selectPicture();
                        break;
                    case R.id.action_take_picture:
                        takePicture();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void takePicture() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE);
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA);
            return;
        }
        EasyImage.openCamera(this, 0);
    }

    private void selectPicture() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_EXTERNAL_STORAGE);
            return;
        }
        EasyImage.openGallery(this, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                e.printStackTrace();
                showAlert(e.getLocalizedMessage());
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                Log.d(TAG, imageFile.getAbsolutePath());
                uploadImage(imageFile);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(EditProfileActivity.this);
                    if (photoFile != null) //noinspection ResultOfMethodCallIgnored
                        photoFile.delete();
                }
            }
        });
    }

    private void uploadImage(final File imageFile) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.design_image, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_user);

        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(imageFile), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap(bitmap);

        new AlertDialog.Builder(this)
                .setTitle("Upload Profile Picture")
                .setView(view)
                .setPositiveButton("UPLOAD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.upload(user.getApiToken(), imageFile);
                    }
                })
                .setNegativeButton("CANCEL", null)
                .setCancelable(false)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { // Permission Granted
                    EasyImage.openGallery(this, 0);
                } else { // Permission Denied
                    showAlert("Storage Read/Write Permission Denied");
                }
                break;
            case PERMISSION_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { // Permission Granted
                    takePicture();
                } else { // Permission Denied
                    showAlert("Storage Read/Write Permission Denied");
                }
                break;
            case PERMISSION_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { // Permission Granted
                    takePicture();
                } else { // Permission Denied
                    showAlert("Camera Permission Denied");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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
                for (Province province : provinceRealmResults) {
                    promoList.add(province.getName());
                    provinceIdList.add(province.getId());
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter<>(EditProfileActivity.this, R.layout.spinner_custom_item, promoList);
                binding.spProvince.setAdapter(arrayAdapter);
                binding.spProvince.setSelection(provinceIdList.indexOf(user.getCity().getProvince_id()));
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

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(EditProfileActivity.this, R.layout.spinner_custom_item, cityNameList);
                binding.spCity.setAdapter(null);
                binding.spCity.setAdapter(arrayAdapter);

                if(isFirstTimeCities){
                    isFirstTimeCities = false;
                    binding.spCity.setSelection(cityIdList.indexOf(user.getCity().getId()));
                }
            }
        });

    }


}
