package tip.edu.ph.runrio.model.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * @author pocholomia
 * @since 12/4/2016
 */
public class RaceKitReservation extends RealmObject {



    @SerializedName("id")
    @PrimaryKey
    private int id;
    @SerializedName("runnerRes_id")
    private String raceKitReservationRunnerId;
    @SerializedName("name")
    private String raceKitReservationName;
     @SerializedName("choices")
    private String raceKitReservationChoices;




    public RaceKitReservation() {
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getRaceKitReservationRunnerId() {
        return raceKitReservationRunnerId;
    }

    public void setRaceKitReservationRunnerId(String raceKitReservationRunnerId) {
        this.raceKitReservationRunnerId = raceKitReservationRunnerId;
    }

    public String getRaceKitReservationName() {
        return raceKitReservationName;
    }

    public void setRaceKitReservationName(String raceKitReservationName) {
        this.raceKitReservationName = raceKitReservationName;
    }

    public String getRaceKitReservationChoices() {
        return raceKitReservationChoices;
    }

    public void setRaceKitReservationChoices(String raceKitReservationChoices) {
        this.raceKitReservationChoices = raceKitReservationChoices;
    }










}
