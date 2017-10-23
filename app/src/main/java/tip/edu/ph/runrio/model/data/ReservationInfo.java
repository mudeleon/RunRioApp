package tip.edu.ph.runrio.model.data;


import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ReservationInfo extends RealmObject {




    @SerializedName("id")
    @PrimaryKey
    private String id;
    @SerializedName("name")
    private String reservationInfoName;
    @SerializedName("desc")
    private String reservationInfoDescription;
    @SerializedName("image")
    private String reservationInfoImage;
    @SerializedName("url")
    private String reservationInfoUrl;
    @SerializedName("address")
    private String reservationInfoAdress;
    @SerializedName("venue")
    private String reservationInfoVenue;
    @SerializedName("lat")
    private String reservationInfoLat;
    @SerializedName("lng")
    private String reservationInfoLng;
    @SerializedName("type")
    private String reservationInfoType;
    @SerializedName("status")
    private String reservationInfoStatus;
    @SerializedName("date")
    private String reservationInfoDate;
    @SerializedName("rules")
    private String reservationInfoRules;
    @SerializedName("overview_link")
    private String reservationInfoSchedule;
    @SerializedName("race_type_text")
    private String raceTypeText;
    @SerializedName("organizers")
    private RealmList<Organizers> organizerCategory;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRaceTypeText() {
        return raceTypeText;
    }

    public void setRaceTypeText(String raceTypeText) {
        this.raceTypeText = raceTypeText;
    }

    public RealmList<Organizers> getOrganizerCategory() {
        return organizerCategory;
    }

    public void setOrganizerCategory(RealmList<Organizers> organizerCategory) {
        this.organizerCategory = organizerCategory;
    }


    public String getReservationInfoName() {
        return reservationInfoName;
    }

    public void setReservationInfoName(String reservationInfoName) {
        this.reservationInfoName = reservationInfoName;
    }

    public String getReservationInfoDescription() {
        return reservationInfoDescription;
    }

    public void setReservationInfoDescription(String reservationInfoDescription) {
        this.reservationInfoDescription = reservationInfoDescription;
    }

    public String getReservationInfoImage() {
        return reservationInfoImage;
    }

    public void setReservationInfoImage(String reservationInfoImage) {
        this.reservationInfoImage = reservationInfoImage;
    }

    public String getReservationInfoUrl() {
        return reservationInfoUrl;
    }

    public void setReservationInfoUrl(String reservationInfoUrl) {
        this.reservationInfoUrl = reservationInfoUrl;
    }

    public String getReservationInfoAdress() {
        return reservationInfoAdress;
    }

    public void setReservationInfoAdress(String reservationInfoAdress) {
        this.reservationInfoAdress = reservationInfoAdress;
    }

    public String getReservationInfoVenue() {
        return reservationInfoVenue;
    }

    public void setReservationInfoVenue(String reservationInfoVenue) {
        this.reservationInfoVenue = reservationInfoVenue;
    }

    public String getReservationInfoLat() {
        return reservationInfoLat;
    }

    public void setReservationInfoLat(String reservationInfoLat) {
        this.reservationInfoLat = reservationInfoLat;
    }

    public String getReservationInfoLng() {
        return reservationInfoLng;
    }

    public void setReservationInfoLng(String reservationInfoLng) {
        this.reservationInfoLng = reservationInfoLng;
    }

    public String getReservationInfoType() {
        return reservationInfoType;
    }

    public void setReservationInfoType(String reservationInfoType) {
        this.reservationInfoType = reservationInfoType;
    }

    public String getReservationInfoStatus() {
        return reservationInfoStatus;
    }

    public void setReservationInfoStatus(String reservationInfoStatus) {
        this.reservationInfoStatus = reservationInfoStatus;
    }

    public String getReservationInfoDate() {
        return reservationInfoDate;
    }

    public void setReservationInfoDate(String reservationInfoDate) {
        this.reservationInfoDate = reservationInfoDate;
    }

    public String getReservationInfoRules() {
        return reservationInfoRules;
    }

    public void setReservationInfoRules(String reservationInfoRules) {
        this.reservationInfoRules = reservationInfoRules;
    }

    public String getReservationInfoSchedule() {
        return reservationInfoSchedule;
    }

    public void setReservationInfoSchedule(String reservationInfoSchedule) {
        this.reservationInfoSchedule = reservationInfoSchedule;
    }


}



