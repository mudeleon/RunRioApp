package tip.edu.ph.runrio.model.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * @author pocholomia
 * @since 12/4/2016
 */
public class DeliveryRate extends RealmObject {



    @SerializedName("id")
    @PrimaryKey
    private int id;
    @SerializedName("claimingType_id")
    private String claimingTypeId;
    @SerializedName("desc1")
    private String deliveryRateOne;
    @SerializedName("desc2")
    private String deliveryRateTwo;


    public DeliveryRate() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClaimingTypeId() {
        return claimingTypeId;
    }

    public void setClaimingTypeId(String claimingTypeId) {
        this.claimingTypeId = claimingTypeId;
    }

    public String getDeliveryRateOne() {
        return deliveryRateOne;
    }

    public void setDeliveryRateOne(String deliveryRateOne) {
        this.deliveryRateOne = deliveryRateOne;
    }

    public String getDeliveryRateTwo() {
        return deliveryRateTwo;
    }

    public void setDeliveryRateTwo(String deliveryRateTwo) {
        this.deliveryRateTwo = deliveryRateTwo;
    }














}
