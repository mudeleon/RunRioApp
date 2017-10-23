package tip.edu.ph.runrio.ui.login;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by pocholomia on 31/08/2016.
 * Interface for Login
 */
public interface LoginView extends MvpView {

    /**
     * Display Alert Dialog
     *
     * @param message message to display
     */
    void showAlert(String message);

    /**
     * Dismiss Progress Dialog
     */
    void dismissLoad();

    void showLoad();

    void startNewActivity();

    void onLogin();

    void onForgotPassword();

    void onRegister();

    void showToast(String message);
}
