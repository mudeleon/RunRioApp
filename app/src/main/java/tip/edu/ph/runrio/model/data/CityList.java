package tip.edu.ph.runrio.model.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class CityList extends RealmObject {

    @SerializedName("id")
    @PrimaryKey
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("province_id")
    private int province_id;

    public CityList() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
