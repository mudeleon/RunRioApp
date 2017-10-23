package tip.edu.ph.runrio.ui.main;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import tip.edu.ph.runrio.model.data.RacesResult;
import tip.edu.ph.runrio.model.data.UpcomingRaces;
import tip.edu.ph.runrio.model.data.Reservation;


public interface RacesListView extends MvpView {

    void setRacesResult();

    void setUpcomingRaces();

    void setUserRaces();

    void intentRacesResult();

    void intentUpcomingRaces();

    void intentUserRaces();

    void stopRefresh();

    void showError(String message);

    void showRacesResultDetails(RacesResult eventRacesResult);

    void showUpcomingRacesDetails(UpcomingRaces eventUpcomingRaces);

    void showUserRacesDetails(Reservation eventReservation);


}
