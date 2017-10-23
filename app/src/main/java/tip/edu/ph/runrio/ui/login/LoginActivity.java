package tip.edu.ph.runrio.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import io.realm.Realm;
import io.realm.RealmResults;
import tip.edu.ph.runrio.BuildConfig;
import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.databinding.ActivityLoginBinding;
import tip.edu.ph.runrio.model.data.User;
import tip.edu.ph.runrio.ui.forgot_password.ForgotPasswordActivity;
import tip.edu.ph.runrio.ui.main.RacesListActivity;
import tip.edu.ph.runrio.ui.register.RegisterActivity;


public class LoginActivity extends MvpViewStateActivity<LoginView, LoginPresenter> implements LoginView {

    private Realm realm; // realm instance
    private ActivityLoginBinding binding; // activity bind
    private ProgressDialog progressDialog;
    private TextView textViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set/bind activity view
        setRetainInstance(true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setView(getMvpView());



        // init progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");
        progressDialog.setCancelable(false);
        realm = Realm.getDefaultInstance(); // realm instance for LoginActivity, close on destroy
        // check if already login
        RealmResults<User> userRealmResults = realm.where(User.class).findAll();
        if (userRealmResults.isValid() && userRealmResults.size() > 0) startNewActivity();
    }

    @Override
    public void startNewActivity() {
        Log.d("login", "start new activity");
        startActivity(new Intent(LoginActivity.this, RacesListActivity.class));
        LoginActivity.this.finish();
    }

    @Override
    public void onLogin() {
        presenter.onLogin(binding.etEmail.getText().toString(), binding.etPassword.getText().toString());
    }

    @Override
    public void onForgotPassword() {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));

    }

    @Override
    public void onRegister() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    @Override
    protected void onDestroy() {
        // removing realm listeners then close instance
        realm.close();
        super.onDestroy();
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void showAlert(String message) {
        new AlertDialog.Builder(this)
                .setTitle(message)
                .setPositiveButton("Close", null)
                .show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message,
                Toast.LENGTH_LONG).show();
    }


    @Override
    public void dismissLoad() {
        progressDialog.dismiss();
    }

    @Override
    public void showLoad() {
        progressDialog.show();
    }

    @NonNull
    @Override
    public ViewState<LoginView> createViewState() {
        setRetainInstance(true);
        return new LoginViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }
}
