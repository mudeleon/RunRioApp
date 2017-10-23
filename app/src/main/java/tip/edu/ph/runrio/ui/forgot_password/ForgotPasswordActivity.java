package tip.edu.ph.runrio.ui.forgot_password;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.databinding.ActivityForgotPasswordBinding;

public class ForgotPasswordActivity
        extends MvpViewStateActivity<ForgotPasswordView, ForgotPasswordPresenter>
        implements ForgotPasswordView, TextWatcher {

    private ActivityForgotPasswordBinding binding;
    private ProgressDialog progressDialog;

    @SuppressWarnings("ConstantConditions") // for toolbar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Reset Password");
        binding.setView(getMvpView());
        binding.etEmail.addTextChangedListener(this);
    }

    @NonNull
    @Override
    public ForgotPasswordPresenter createPresenter() {
        return new ForgotPasswordPresenter();
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
    public void showAlert(String message) {
        new AlertDialog.Builder(this)
                .setTitle(message)
                .setPositiveButton("Close", null)
                .show();
    }

    @Override
    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Connecting...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    @Override
    public void dismissProgress() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    @Override
    public void forgotPasswordSuccessful(String message) {
        new AlertDialog.Builder(this)
                .setTitle(message)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ForgotPasswordActivity.this.finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void onForgotPasswordClicked() {
        presenter.forgotPassword(binding.etEmail.getText().toString());
    }

    @Override
    public void setEmailText(String email) {
        binding.etEmail.setText(email);
    }

    @NonNull
    @Override
    public ViewState<ForgotPasswordView> createViewState() {
        setRetainInstance(true);
        return new ForgotPasswordViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        binding.etEmail.setText("");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        ((ForgotPasswordViewState) getViewState()).setEmail(binding.etEmail.getText().toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
