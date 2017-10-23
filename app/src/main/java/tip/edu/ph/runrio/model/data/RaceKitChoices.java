package tip.edu.ph.runrio.model.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * @author pocholomia
 * @since 12/4/2016
 */
public class RaceKitChoices extends RealmObject {



    @SerializedName("id")
    @PrimaryKey
    private int id;
    @SerializedName("raceKit_id")
    private String raceKitChoicesKitId;
    @SerializedName("choice")
    private String raceKitChoicesChoice;
    @SerializedName("desc")
    private String raceKitChoicesDesc;





    public RaceKitChoices() {
    }

    public String getRaceKitChoicesKitId() {
        return raceKitChoicesKitId;
    }

    public void setRaceKitChoicesKitId(String raceKitChoicesKitId) {
        this.raceKitChoicesKitId = raceKitChoicesKitId;
    }

    public String getRaceKitChoicesChoice() {
        return raceKitChoicesChoice;
    }

    public void setRaceKitChoicesChoice(String raceKitChoicesChoice) {
        this.raceKitChoicesChoice = raceKitChoicesChoice;
    }

    public String getRaceKitChoicesDesc() {
        return raceKitChoicesDesc;
    }

    public void setRaceKitChoicesDesc(String raceKitChoicesDesc) {
        this.raceKitChoicesDesc = raceKitChoicesDesc;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }










}
