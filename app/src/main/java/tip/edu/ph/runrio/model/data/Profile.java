package tip.edu.ph.runrio.model.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import tip.edu.ph.runrio.app.Constants;


/**
 * @author pocholomia
 * @since 12/4/2016
 */
public class Profile extends RealmObject {



    @PrimaryKey
    @SerializedName("id")
    private int id;
    @SerializedName("first_name")
    private String profileFirstName;
    @SerializedName("last_name")
    private String profileLastName;
    @SerializedName("shirt_size")
    private String profileShirtSize;
    @SerializedName("bday")
    private String profileBday;
    @SerializedName("gender")
    private String profileGender;
    @SerializedName("email")
    private String profileEmail;
    @SerializedName("contact_prefix")
    private String profileContactPrefix;
    @SerializedName("contact_no")
    private String profileContactNo;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfileFirstName() {
        return profileFirstName;
    }

    public void setProfileFirstName(String profileFirstName) {
        this.profileFirstName = profileFirstName;
    }

    public String getProfileLastName() {
        return profileLastName;
    }

    public void setProfileLastName(String profileLastName) {
        this.profileLastName = profileLastName;
    }

    public String getProfileShirtSize() {
        return profileShirtSize;
    }

    public void setProfileShirtSize(String profileShirtSize) {
        this.profileShirtSize = profileShirtSize;
    }

    public String getProfileBday() {
        return profileBday;
    }

    public void setProfileBday(String profileBday) {
        this.profileBday = profileBday;
    }

    public String getProfileGender() {
        return profileGender;
    }

    public void setProfileGender(String profileGender) {
        this.profileGender = profileGender;
    }

    public String getProfileEmail() {
        return profileEmail;
    }

    public void setProfileEmail(String profileEmail) {
        this.profileEmail = profileEmail;
    }

    public String getProfileContactPrefix() {
        return profileContactPrefix;
    }

    public void setProfileContactPrefix(String profileContactPrefix) {
        this.profileContactPrefix = profileContactPrefix;
    }

    public String getProfileContactNo() {
        return profileContactNo;
    }

    public void setProfileContactNo(String profileContactNo) {
        this.profileContactNo = profileContactNo;
    }


    public String getFullName() {
        return profileFirstName + " " + profileLastName;
    }

}
