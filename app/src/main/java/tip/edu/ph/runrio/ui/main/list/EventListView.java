package tip.edu.ph.runrio.ui.main.list;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import tip.edu.ph.runrio.model.data.RacesResult;
import tip.edu.ph.runrio.model.data.Reservation;
import tip.edu.ph.runrio.model.data.UpcomingRaces;


public interface EventListView extends MvpView {






    void setRacesResult();

    void setUpcomingRaces();

    void setUserRaces();

    void showRacesResult(RacesResult eventRacesResult);

    void showUpcomingRaces(UpcomingRaces eventUpcomingRaces);

    void showUserRaces(Reservation eventReservation);

    void showRacesResultDetails(RacesResult eventRacesResult);

    void showUpcomingRacesDetails(UpcomingRaces eventUpcomingRaces);

    void showUserRacesDetails(Reservation eventReservation);

    void stopRefresh();

    void showError(String message);




}
