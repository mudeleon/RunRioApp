package tip.edu.ph.runrio.ui.profile.edit;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * @author pocholomia
 * @since 02/11/2016
 */
interface EditProfileView extends MvpView {
    void showAlert(String message);

    void showLoading();

    void stopLoading();

    void updateAccountSuccessful(String message);
}
