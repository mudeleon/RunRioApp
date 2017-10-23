package tip.edu.ph.runrio.model.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * @author pocholomia
 * @since 12/4/2016
 */
public class ClaimingReservation extends RealmObject {



    @SerializedName("id")
    @PrimaryKey
    private int id;
    @SerializedName("description")
    private String claimingReservationDescription;
    @SerializedName("price")
    private String claimingReservationPrice;
    @SerializedName("barangay")
    private String claimingReservationBarangay;
    @SerializedName("zip_code")
    private String claimingReservationZip;
    @SerializedName("address")
    private String claimingReservationAddress;
    @SerializedName("type")
    private String claimingReservationType;
    @SerializedName("city")
    private City city;

    public ClaimingReservation() {
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClaimingReservationDescription() {
        return claimingReservationDescription;
    }

    public void setClaimingReservationDescription(String claimingReservationDescription) {
        this.claimingReservationDescription = claimingReservationDescription;
    }

    public String getClaimingReservationPrice() {
        return claimingReservationPrice;
    }

    public void setClaimingReservationPrice(String claimingReservationPrice) {
        this.claimingReservationPrice = claimingReservationPrice;
    }

    public String getClaimingReservationBarangay() {
        return claimingReservationBarangay;
    }

    public void setClaimingReservationBarangay(String claimingReservationBarangay) {
        this.claimingReservationBarangay = claimingReservationBarangay;
    }

    public String getClaimingReservationZip() {
        return claimingReservationZip;
    }

    public void setClaimingReservationZip(String claimingReservationZip) {
        this.claimingReservationZip = claimingReservationZip;
    }

    public String getClaimingReservationAddress() {
        return claimingReservationAddress;
    }

    public void setClaimingReservationAddress(String claimingReservationAddress) {
        this.claimingReservationAddress = claimingReservationAddress;
    }

    public String getClaimingReservationType() {
        return claimingReservationType;
    }

    public void setClaimingReservationType(String claimingReservationType) {
        this.claimingReservationType = claimingReservationType;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }















}
