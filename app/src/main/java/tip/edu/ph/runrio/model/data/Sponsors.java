package tip.edu.ph.runrio.model.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * @author pocholomia
 * @since 12/4/2016
 */
public class Sponsors extends RealmObject {



    @SerializedName("id")
    @PrimaryKey
    private int id;
    @SerializedName("name")
    private String sponsorsName;
    @SerializedName("details")
    private String sponsorsDetails;
    @SerializedName("image")
    private String sponsorsImage;


    public Sponsors() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSponsorsName() {
        return sponsorsName;
    }

    public void setSponsorsName(String sponsorsName) {
        this.sponsorsName = sponsorsName;
    }

    public String getSponsorsDetails() {
        return sponsorsDetails;
    }

    public void setSponsorsDetails(String sponsorsDetails) {
        this.sponsorsDetails = sponsorsDetails;
    }

    public String getSponsorsImage() {
        return sponsorsImage;
    }

    public void setSponsorsImage(String sponsorsImage) {
        this.sponsorsImage = sponsorsImage;
    }















}
