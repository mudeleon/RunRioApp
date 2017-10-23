package tip.edu.ph.runrio.model.data;


import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Reservation extends RealmObject {




    @SerializedName("id")
    @PrimaryKey
    private String id;
    @SerializedName("reservation_status")
    private String reservationStatus;
    @SerializedName("payment_status")
    private String reservationPaymentStatus;
    @SerializedName("date_reserved")
    private String reservationDate;



    @SerializedName("event")
    private ReservationInfo reservationReservationInfoCategory;
    @SerializedName("claiming_reservation")
    private ClaimingReservation reservationClaimingReservationCategory;
    @SerializedName("runner_reservations")
    private RealmList<RunnerReservation> reservationRunnerReservationCategory;


    public ReservationInfo getReservationReservationInfoCategory() {
        return reservationReservationInfoCategory;
    }

    public void setReservationReservationInfoCategory(ReservationInfo reservationReservationInfoCategory) {
        this.reservationReservationInfoCategory = reservationReservationInfoCategory;
    }

    public ClaimingReservation getReservationClaimingReservationCategory() {
        return reservationClaimingReservationCategory;
    }

    public void setReservationClaimingReservationCategory(ClaimingReservation reservationClaimingReservationCategory) {
        this.reservationClaimingReservationCategory = reservationClaimingReservationCategory;
    }
    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public String getReservationPaymentStatus() {
        return reservationPaymentStatus;
    }

    public void setReservationPaymentStatus(String reservationPaymentStatus) {
        this.reservationPaymentStatus = reservationPaymentStatus;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }



    public RealmList<RunnerReservation> getReservationRunnerReservationCategory() {
        return reservationRunnerReservationCategory;
    }

    public void setReservationRunnerReservationCategory(RealmList<RunnerReservation> reservationRunnerReservationCategory) {
        this.reservationRunnerReservationCategory = reservationRunnerReservationCategory;
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }









}



