package tip.edu.ph.runrio.ui.upcoming_race.detail;

import com.hannesdorfmann.mosby.mvp.MvpView;

import tip.edu.ph.runrio.model.data.UpcomingRaces;

/**
 * @author pocholomia
 * @since 03/10/2016
 */

public interface UpcomingRaceDetailView extends MvpView {
    void stopRefresh();

    void showError(String message);

    void setUpcomingRaces();

    void showRules(UpcomingRaces races);

    void showSchedules(UpcomingRaces races);

    void reserve(UpcomingRaces races);

}
