package tip.edu.ph.runrio.ui.upcoming_race.transaction.racetype;

import com.hannesdorfmann.mosby.mvp.MvpView;

import tip.edu.ph.runrio.model.data.RaceType;
import tip.edu.ph.runrio.model.data.UpcomingRaces;

/**
 * @author pocholomia
 * @since 03/10/2016
 */

public interface RaceTypeTransactionView extends MvpView {
    void stopRefresh();

    void showError(String message);

    void setUpcomingRaces();

    void soloRace(RaceType races);


    void switchView(boolean check);

    void clickNext();





}
