package tip.edu.ph.runrio.model.data;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TransactionData extends RealmObject {




    @SerializedName("id")
    @PrimaryKey
    private int id;

    private String transactionDataProfileId;

    private RealmList<RealmString> transactionDataRaceKit;

    private RealmList<RealmString2> transactionDataRaceKitChoices;


    private boolean nullChecker;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransactionDataProfileId() {
        return transactionDataProfileId;
    }

    public void setTransactionDataProfileId(String transactionDataProfileId) {
        this.transactionDataProfileId = transactionDataProfileId;
    }


    public boolean isNullChecker() {
        return nullChecker;
    }

    public void setNullChecker(boolean nullChecker) {
        this.nullChecker = nullChecker;
    }

    public RealmList<RealmString> getTransactionDataRaceKit() {
        return transactionDataRaceKit;
    }

    public void setTransactionDataRaceKit(RealmList<RealmString> transactionDataRaceKit) {
        this.transactionDataRaceKit = transactionDataRaceKit;
    }

    public RealmList<RealmString2> getTransactionDataRaceKitChoices() {
        return transactionDataRaceKitChoices;
    }

    public void     setTransactionDataRaceKitChoices(RealmList<RealmString2> transactionDataRaceKitChoices) {
        this.transactionDataRaceKitChoices = transactionDataRaceKitChoices;
    }



}



