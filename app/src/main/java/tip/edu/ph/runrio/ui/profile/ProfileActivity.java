package tip.edu.ph.runrio.ui.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.app.Endpoints;
import tip.edu.ph.runrio.databinding.ActivityProfileBinding;
import tip.edu.ph.runrio.databinding.DialogChangePasswordBinding;
import tip.edu.ph.runrio.model.data.User;
import tip.edu.ph.runrio.ui.profile.edit.EditProfileActivity;
import tip.edu.ph.runrio.util.CircleTransform;


public class ProfileActivity extends MvpViewStateActivity<ProfileView, ProfilePresenter>
        implements ProfileView, SwipeRefreshLayout.OnRefreshListener {

    private ActivityProfileBinding binding;
    private Realm realm;
    private User user;
    private ProgressDialog progressDialog;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        realm = Realm.getDefaultInstance();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Profile");
        //binding.swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_layout_color_scheme));
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        user = realm.where(User.class).findFirstAsync();
        user.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                if (user.isLoaded() && user.isValid()) {
                    binding.setUser(user);
                    String imageURL = "https://payapp.tip.edu.ph/api/storage/app/image/default_buyer.png";
                    if (user.getImage() != null && !user.getImage().isEmpty()) {
                        imageURL = Endpoints.IMAGE_URL.replace(Endpoints.IMG_HOLDER, user.getImage());
                        Log.d("TAG",imageURL);
                    }
                    if (user.getFbID() != null && !user.getFbID().isEmpty()) {
                        imageURL = Endpoints.IMAGE_URL_FB.replace(Endpoints.IMG_HOLDER, user.getFbID());
                    }
                    Glide.with(ProfileActivity.this)
                            .load(imageURL)
                            .transform(new CircleTransform(ProfileActivity.this))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(binding.layoutHeader.imageView);
                }
            }
        });
      //  binding.setView(getMvpView()); <<<<<<<<<<<<<<<
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
            case R.id.action_edit_profile:
                startActivity(new Intent(this, EditProfileActivity.class));
                return true;
            case R.id.action_message:
                Toast.makeText(this, "Coming Soon..", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        user.removeChangeListeners();
        realm.close();
        super.onDestroy();
    }

    @NonNull
    @Override
    public ProfilePresenter createPresenter() {
        return new ProfilePresenter();
    }

    @NonNull
    @Override
    public ViewState<ProfileView> createViewState() {
        setRetainInstance(true);
        return new ProfileViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }

    @Override
    public void onRefresh() {
        // todo create refresh
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onChangePasswordClicked() {
        final DialogChangePasswordBinding dialogBinding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.dialog_change_password, null, false);
        final AlertDialog builder = new AlertDialog.Builder(this)
                .setView(dialogBinding.getRoot())
                .create();
        dialogBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword = dialogBinding.etPassword.getText().toString();
                String newPassword = dialogBinding.etNewPassword.getText().toString();
                String repeatPassword = dialogBinding.etRepeatPassword.getText().toString();
                if (oldPassword.isEmpty() || newPassword.isEmpty() || repeatPassword.isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Fill-up all fields", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.contentEquals(repeatPassword)) {
                    Toast.makeText(ProfileActivity.this, "Passwords does not match", Toast.LENGTH_SHORT).show();
                } else {
                    presenter.changePassword(user.getApiToken(), user.getEmailAddress(), oldPassword, newPassword);
                    builder.dismiss();
                }
            }
        });
        //noinspection ConstantConditions
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        builder.show();
    }

    @Override
    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
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
        new AlertDialog.Builder(this)
                .setTitle(message)
                .setPositiveButton("Close", null)
                .show();
    }
}
