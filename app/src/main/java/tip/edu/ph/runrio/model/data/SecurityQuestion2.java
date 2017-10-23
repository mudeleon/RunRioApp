package tip.edu.ph.runrio.model.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class SecurityQuestion2 extends RealmObject {

    @SerializedName("id")
    @PrimaryKey
    private int id;
    @SerializedName("description")
    private String description;
    @SerializedName("set_number")
    private int set_number;

    public SecurityQuestion2() {
    }

    public int getSet_number() {
        return set_number;
    }

    public void setSet_number(int set_number) {
        this.set_number = set_number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
