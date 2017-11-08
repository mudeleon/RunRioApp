package tip.edu.ph.runrio.ui.reservations;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import io.realm.Realm;
import tip.edu.ph.runrio.app.Constants;
import tip.edu.ph.runrio.model.data.Reservation;
import tip.edu.ph.runrio.model.data.UpcomingRaces;


/**
 * Created by itsodeveloper on 10/10/2017.
 */

class ReservationsPresenter extends MvpNullObjectBasePresenter<ReservationsView> {

    private Realm realm;

    void onStart() {
        realm = Realm.getDefaultInstance();
    }

  /*  public Reservation getReservation(int id){
        realm.where(Reservation.class).
    }*/

    public Reservation raceEvent(int id) {
        return realm.where(Reservation.class).equalTo(Constants.ID, id).findFirst();
    }

    void onStop() {
        realm.close();
    }
}
