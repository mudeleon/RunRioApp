package tip.edu.ph.runrio.ui.reservations.detail;

import com.hannesdorfmann.mosby.mvp.MvpView;

import tip.edu.ph.runrio.model.data.Reservation;

/**
 * Created by itsodeveloper on 11/10/2017.
 */

public interface ReservationDetailView extends MvpView {
    void onViewAllRunners(Reservation res);
}
