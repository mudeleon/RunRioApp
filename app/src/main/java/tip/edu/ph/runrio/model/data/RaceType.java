package tip.edu.ph.runrio.model.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * @author pocholomia
 * @since 12/4/2016
 */
public class RaceType extends RealmObject {




    @PrimaryKey
    @SerializedName("id")
    private int id;
     @SerializedName("event_id")
    private String raceTypeRaceId;
    @SerializedName("name")
    private String raceTypeName;
    @SerializedName("int_fee")
    private String raceTypeIntFee;
    @SerializedName("local_fee")
    private String raceTypeLocFee;
    @SerializedName("slot_alloted")
    private String raceTypeSlotAlot;
    @SerializedName("slot_available")
    private String raceTypeSlotAvail;
    @SerializedName("assembly_time")
    private String raceTypeAssemblyTime;
    @SerializedName("gun_start")
    private String raceTypeGunStart;
    @SerializedName("cut_off_time")
    private String raceTypeCutOff;


    @SerializedName("race_kit")
    private RealmList<RaceKit> racekitCategory;

    public RaceType() {
    }

    public RealmList<RaceKit> getRacekitCategory() {
        return racekitCategory;
    }

    public void setRacekitCategory(RealmList<RaceKit> racekitCategory) {
        this.racekitCategory = racekitCategory;
    }


    public String getRaceTypeAssemblyTime() {
        return raceTypeAssemblyTime;
    }

    public void setRaceTypeAssemblyTime(String raceTypeAssemblyTime) {
        this.raceTypeAssemblyTime = raceTypeAssemblyTime;
    }

    public String getRaceTypeGunStart() {
        return raceTypeGunStart;
    }

    public void setRaceTypeGunStart(String raceTypeGunStart) {
        this.raceTypeGunStart = raceTypeGunStart;
    }

    public String getRaceTypeCutOff() {
        return raceTypeCutOff;
    }

    public void setRaceTypeCutOff(String raceTypeCutOff) {
        this.raceTypeCutOff = raceTypeCutOff;
    }

    public String getRaceTypeRaceId() {
        return raceTypeRaceId;
    }

    public void setRaceTypeRaceId(String raceTypeRaceId) {
        this.raceTypeRaceId = raceTypeRaceId;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRaceTypeName() {
        return raceTypeName;
    }

    public void setRaceTypeName(String raceTypeName) {
        this.raceTypeName = raceTypeName;
    }

    public String getRaceTypeIntFee() {
        return raceTypeIntFee;
    }

    public void setRaceTypeIntFee(String raceTypeIntFee) {
        this.raceTypeIntFee = raceTypeIntFee;
    }

    public String getRaceTypeLocFee() {
        return raceTypeLocFee;
    }

    public void setRaceTypeLocFee(String raceTypeLocFee) {
        this.raceTypeLocFee = raceTypeLocFee;
    }

    public String getRaceTypeSlotAlot() {
        return raceTypeSlotAlot;
    }

    public void setRaceTypeSlotAlot(String raceTypeSlotAlot) {
        this.raceTypeSlotAlot = raceTypeSlotAlot;
    }

    public String getRaceTypeSlotAvail() {
        return raceTypeSlotAvail;
    }

    public void setRaceTypeSlotAvail(String raceTypeSlotAvail) {
        this.raceTypeSlotAvail = raceTypeSlotAvail;
    }








}
