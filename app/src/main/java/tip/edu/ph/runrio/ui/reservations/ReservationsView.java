package tip.edu.ph.runrio.ui.reservations;

import com.hannesdorfmann.mosby.mvp.MvpView;

import tip.edu.ph.runrio.model.data.Reservation;


/**
 * Created by itsodeveloper on 10/10/2017.
 */

public interface ReservationsView extends MvpView{
    void onItemClicked(Reservation reservation);
}
