package tip.edu.ph.runrio.ui.forgot_password;

import com.hannesdorfmann.mosby.mvp.MvpView;


public interface ForgotPasswordView extends MvpView {
    void showAlert(String message);

    void showProgress();

    void dismissProgress();

    void forgotPasswordSuccessful(String message);

    void onForgotPasswordClicked();

    void setEmailText(String email);
}
