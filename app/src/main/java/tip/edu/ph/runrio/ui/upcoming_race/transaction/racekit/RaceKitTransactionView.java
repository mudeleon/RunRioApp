package tip.edu.ph.runrio.ui.upcoming_race.transaction.racekit;

import com.hannesdorfmann.mosby.mvp.MvpView;

import tip.edu.ph.runrio.model.data.RaceType;

/**
 * @author pocholomia
 * @since 03/10/2016
 */

public interface RaceKitTransactionView extends MvpView {
    void stopRefresh();

    void showError(String message);

    void setUpcomingRaces();

    void soloRace(RaceType races);


    void switchView(boolean check);

    void clickNext();





}
