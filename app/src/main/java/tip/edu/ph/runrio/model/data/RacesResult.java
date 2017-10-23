package tip.edu.ph.runrio.model.data;


import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RacesResult  extends RealmObject {




    @SerializedName("id")
    @PrimaryKey
    private String id;
    @SerializedName("name")
    private String racesName;
    @SerializedName("desc")
    private String raceDescription;
    @SerializedName("image")
    private String raceImage;
    @SerializedName("url")
    private String raceUrl;
    @SerializedName("address")
    private String raceAdress;
    @SerializedName("venue")
    private String raceVenue;
    @SerializedName("lat")
    private String raceLat;
    @SerializedName("lng")
    private String raceLng;
    @SerializedName("type")
    private String raceType;
    @SerializedName("status")
    private String raceStatus;
    @SerializedName("date")
    private String raceDate;
    @SerializedName("race_type_text")
    private String raceTypeText;

    public String getRaceTypeText() {
        return raceTypeText;
    }

    public void setRaceTypeText(String raceTypeText) {
        this.raceTypeText = raceTypeText;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRacesName() {
        return racesName;
    }

    public void setRacesName(String racesName) {
        this.racesName = racesName;
    }

    public String getRaceDescription() {
        return raceDescription;
    }

    public void setRaceDescription(String raceDescription) {
        this.raceDescription = raceDescription;
    }

    public String getRaceImage() {
        return raceImage;
    }

    public void setRaceImage(String raceImage) {
        this.raceImage = raceImage;
    }

    public String getRaceUrl() {
        return raceUrl;
    }

    public void setRaceUrl(String raceUrl) {
        this.raceUrl = raceUrl;
    }

    public String getRaceAdress() {
        return raceAdress;
    }

    public void setRaceAdress(String raceAdress) {
        this.raceAdress = raceAdress;
    }

    public String getRaceVenue() {
        return raceVenue;
    }

    public void setRaceVenue(String raceVenue) {
        this.raceVenue = raceVenue;
    }

    public String getRaceLat() {
        return raceLat;
    }

    public void setRaceLat(String raceLat) {
        this.raceLat = raceLat;
    }

    public String getRaceLng() {
        return raceLng;
    }

    public void setRaceLng(String raceLng) {
        this.raceLng = raceLng;
    }

    public String getRaceType() {
        return raceType;
    }

    public void setRaceType(String raceType) {
        this.raceType = raceType;
    }

    public String getRaceStatus() {
        return raceStatus;
    }

    public void setRaceStatus(String raceStatus) {
        this.raceStatus = raceStatus;
    }

    public String getRaceDate() {
        return raceDate;
    }

    public void setRaceDate(String raceDate) {
        this.raceDate = raceDate;
    }







}



