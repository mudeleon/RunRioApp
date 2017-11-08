package tip.edu.ph.runrio.ui.upcoming_race.transaction.expense;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * @author pocholomia
 * @since 03/10/2016
 */

public interface ExpenseView extends MvpView {
    void stopRefresh();

    void showError(String message);

    void onReserveSuccess();
    void reserve();

    void showProgress();

    void dismissProgress();

}
