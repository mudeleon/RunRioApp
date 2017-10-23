package tip.edu.ph.runrio.ui.register;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * @author pocholomia
 * @since 08/09/2016
 */
public interface RegisterView extends MvpView {
    void showAlert(String message);

    void showProgress();

    void dismissProgress();

    void registerSuccessful(String message);

    void onRegister();

    void onSelectBirthday();
}
