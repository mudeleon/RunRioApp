package tip.edu.ph.runrio.ui.runner.list;

import com.hannesdorfmann.mosby.mvp.MvpView;

import tip.edu.ph.runrio.model.data.Profile;




public interface RunnerListView extends MvpView {

    void stopRefresh();

    void showError(String message);

    void onRunnerClicked(Profile runner);

    void setRunnerList();

    void prepareList();

    void onSaveAdd(Profile runner);

    void onSuccess();

    void onEditProfile(Profile runner);

    void onDeleteProfile(Profile runner);

    void onSelectBirthday();

    void showProgress();

    void dismissProgress();

}
