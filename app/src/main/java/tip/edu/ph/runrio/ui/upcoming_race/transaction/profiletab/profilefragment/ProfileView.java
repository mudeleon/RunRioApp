package tip.edu.ph.runrio.ui.upcoming_race.transaction.profiletab.profilefragment;

import com.hannesdorfmann.mosby.mvp.MvpView;

import tip.edu.ph.runrio.model.data.Profile;


public interface ProfileView extends MvpView {


    void onDialogRunnerClicked(Profile runner);

    void onSaveChoices();

    void onAddRunner(Profile runner);

    void showProgress();

    void stopProgress();

    void showAlert(String message);
}
