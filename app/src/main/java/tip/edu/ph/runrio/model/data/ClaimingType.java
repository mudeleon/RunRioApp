package tip.edu.ph.runrio.model.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * @author pocholomia
 * @since 12/4/2016
 */
public class ClaimingType extends RealmObject {



    @SerializedName("id")
    @PrimaryKey
    private int id;
     @SerializedName("event_id")
    private String claimingTypeRaceId;
    @SerializedName("name")
    private String claimingTypeName;
    @SerializedName("description")
    private String claimingTypeDescription;
    @SerializedName("type")
    private String claimingTypeType;
    @SerializedName("address")
    private String claimingTypeAddress;
    @SerializedName("delivery_rate")
     private RealmList<DeliveryRate> deliveryRateCategory;

    public ClaimingType() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClaimingTypeRaceId() {
        return claimingTypeRaceId;
    }

    public void setClaimingTypeRaceId(String claimingTypeRaceId) {
        this.claimingTypeRaceId = claimingTypeRaceId;
    }

    public String getClaimingTypeName() {
        return claimingTypeName;
    }

    public void setClaimingTypeName(String claimingTypeName) {
        this.claimingTypeName = claimingTypeName;
    }

    public String getClaimingTypeDescription() {
        return claimingTypeDescription;
    }

    public void setClaimingTypeDescription(String claimingTypeDescription) {
        this.claimingTypeDescription = claimingTypeDescription;
    }

    public String getClaimingTypeType() {
        return claimingTypeType;
    }

    public void setClaimingTypeType(String claimingTypeType) {
        this.claimingTypeType = claimingTypeType;
    }

    public String getClaimingTypeAddress() {
        return claimingTypeAddress;
    }

    public void setClaimingTypeAddress(String claimingTypeAddress) {
        this.claimingTypeAddress = claimingTypeAddress;
    }

    public RealmList<DeliveryRate> getDeliveryRateCategory() {
        return deliveryRateCategory;
    }

    public void setDeliveryRateCategory(RealmList<DeliveryRate> deliveryRateCategory) {
        this.deliveryRateCategory = deliveryRateCategory;
    }













}
