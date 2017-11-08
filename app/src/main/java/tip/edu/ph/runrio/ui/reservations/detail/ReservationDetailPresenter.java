package tip.edu.ph.runrio.ui.reservations.detail;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import io.realm.Realm;
import tip.edu.ph.runrio.app.Constants;
import tip.edu.ph.runrio.model.data.Reservation;

/**
 * Created by itsodeveloper on 11/10/2017.
 */

class ReservationDetailPresenter extends MvpNullObjectBasePresenter<ReservationDetailView>{


    private Realm realm;

    void onStart() {
        realm = Realm.getDefaultInstance();
    }

  /*  public Reservation getReservation(int id){
        realm.where(Reservation.class).
    }*/

    public Reservation reservation(int id) {
        return realm.where(Reservation.class).equalTo(Constants.ID, id).findFirst();
    }

    void onStop() {
        realm.close();
    }
}
