package tip.edu.ph.runrio.model.data;


import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RunnerReservation extends RealmObject {


    @SerializedName("id")
    @PrimaryKey
    private String id;
    @SerializedName("race_type")
    private RealmList<RaceType> racetypeCategory;
    @SerializedName("race_kit_reservations")
    private RealmList<RaceKitReservation> raceKitReservationCategory;
    @SerializedName("participant_profile")
    private RealmList<Profile> profileCategory;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RealmList<RaceType> getRacetypeCategory() {
        return racetypeCategory;
    }

    public void setRacetypeCategory(RealmList<RaceType> racetypeCategory) {
        this.racetypeCategory = racetypeCategory;
    }

    public RealmList<RaceKitReservation> getRaceKitReservationCategory() {
        return raceKitReservationCategory;
    }

    public void setRaceKitReservationCategory(RealmList<RaceKitReservation> raceKitReservationCategory) {
        this.raceKitReservationCategory = raceKitReservationCategory;
    }

    public RealmList<Profile> getProfileCategory() {
        return profileCategory;
    }

    public void setProfileCategory(RealmList<Profile> profileCategory) {
        this.profileCategory = profileCategory;
    }








}



