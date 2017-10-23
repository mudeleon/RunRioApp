package tip.edu.ph.runrio.ui.profile;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * @author pocholomia
 * @since 02/11/2016
 */

public interface ProfileView extends MvpView {

    void onChangePasswordClicked();

    void showProgress();

    void stopProgress();

    void showAlert(String message);
}
