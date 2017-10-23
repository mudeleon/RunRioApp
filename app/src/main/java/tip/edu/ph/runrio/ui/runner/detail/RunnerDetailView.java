package tip.edu.ph.runrio.ui.runner.detail;

import com.hannesdorfmann.mosby.mvp.MvpView;



public interface RunnerDetailView extends MvpView {
    void showProgress();

    void hideProgress();

    void showAlert(String message);
}
