package tip.edu.ph.runrio.model.data;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmString2 extends RealmObject {

    @PrimaryKey
    private long id;
    private String val;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}

