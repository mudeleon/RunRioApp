package tip.edu.ph.runrio.model.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * @author pocholomia
 * @since 12/4/2016
 */
public class RaceKit extends RealmObject {




    @PrimaryKey
    @SerializedName("id")
    private int id;
    @SerializedName("raceType_id")
    private String raceKitTypeId;
    @SerializedName("name")
    private String raceKitName;
    @SerializedName("notes")
    private String raceKitNotes;
    @SerializedName("rules")
    private String raceKitRules;
    @SerializedName("image")
    private String raceKitImage;
    private RealmList<RaceKitChoices> raceKitChoicesCategory;




    public RaceKit() {
    }


    public String getRaceKitImage() {
        return raceKitImage;
    }

    public void setRaceKitImage(String raceKitImage) {
        this.raceKitImage = raceKitImage;
    }
    public String getRaceKitTypeId() {
        return raceKitTypeId;
    }

    public void setRaceKitTypeId(String raceKitTypeId) {
        this.raceKitTypeId = raceKitTypeId;
    }

    public String getRaceKitName() {
        return raceKitName;
    }

    public void setRaceKitName(String raceKitName) {
        this.raceKitName = raceKitName;
    }

    public String getRaceKitNotes() {
        return raceKitNotes;
    }

    public void setRaceKitNotes(String raceKitNotes) {
        this.raceKitNotes = raceKitNotes;
    }

    public String getRaceKitRules() {
        return raceKitRules;
    }

    public void setRaceKitRules(String raceKitRules) {
        this.raceKitRules = raceKitRules;
    }


    public RealmList<RaceKitChoices> getRaceKitChoicesCategory() {
        return raceKitChoicesCategory;
    }

    public void setRaceKitChoicesCategory(RealmList<RaceKitChoices> raceKitChoicesCategory) {
        this.raceKitChoicesCategory = raceKitChoicesCategory;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }










}
