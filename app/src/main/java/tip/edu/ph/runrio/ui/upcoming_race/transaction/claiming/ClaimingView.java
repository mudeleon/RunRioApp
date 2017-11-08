package tip.edu.ph.runrio.ui.upcoming_race.transaction.claiming;

import com.hannesdorfmann.mosby.mvp.MvpView;

import tip.edu.ph.runrio.model.data.UpcomingRaces;

/**
 * @author pocholomia
 * @since 03/10/2016
 */

public interface ClaimingView extends MvpView {
    void stopRefresh();

    void showError(String message);

    void showTerms();

    void showWaiver();

    void reserve();

}
