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
    private RaceType racetypeCategory;
    @SerializedName("race_kit_reservations")
    private RealmList<RaceKitReservation> raceKitReservationCategory;


    @SerializedName("participant_profile")
    private Profile profileCategory;


    public int getBibNum() {
        return bibNum;
    }

    public void setBibNum(int bibNum) {
        this.bibNum = bibNum;
    }

    @SerializedName("bib_number")
    private int bibNum;

    public RaceType getRacetypeCategory() {
        return racetypeCategory;
    }

    public void setRacetypeCategory(RaceType racetypeCategory) {
        this.racetypeCategory = racetypeCategory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public RealmList<RaceKitReservation> getRaceKitReservationCategory() {
        return raceKitReservationCategory;
    }

    public void setRaceKitReservationCategory(RealmList<RaceKitReservation> raceKitReservationCategory) {
        this.raceKitReservationCategory = raceKitReservationCategory;
    }

    public Profile getProfileCategory() {
        return profileCategory;
    }

    public void setProfileCategory(Profile profileCategory) {
        this.profileCategory = profileCategory;
    }








}



