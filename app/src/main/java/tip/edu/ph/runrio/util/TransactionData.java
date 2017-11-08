package tip.edu.ph.runrio.util;

/**
 * Created by itsodeveloper on 30/10/2017.
 */
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.List;

public class TransactionData {


        public static final String TAG = tip.edu.ph.runrio.model.data.TransactionData.class.getSimpleName();

        private int profileID;
        private String dataRaceKit;
        private String dataRaceKitChoices;



        public static TransactionData getAppSettingsFromSharedPreference(Context context, int ctr) {
            TransactionData appSettings = new TransactionData();

            SharedPreferences settings = context.getSharedPreferences(TAG, Context.MODE_MULTI_PROCESS);


            appSettings.profileID = settings.getInt("profile_id"+ctr, -1);
            appSettings.dataRaceKit = settings.getString("dataRaceKit"+ctr, "");
            appSettings.dataRaceKitChoices = settings.getString("dataRaceKitChoices"+ctr, "");

            return appSettings;
        }



        public void save(Context context, int ctr) {

            SharedPreferences settings = context.getSharedPreferences(TAG, Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor editor = settings.edit();


            editor.putInt("profile_id"+ctr, profileID);
            editor.putString("dataRaceKit"+ctr, dataRaceKit);
            editor.putString("dataRaceKitChoices"+ctr, dataRaceKitChoices);

            editor.commit();
            //editor.apply();
        }




    public int getProfileID() {
        return profileID;
    }

    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    public String getDataRaceKit() {
        return dataRaceKit;
    }

    public void setDataRaceKit(String dataRaceKit) {
        this.dataRaceKit = dataRaceKit;
    }

    public String getDataRaceKitChoices() {
        return dataRaceKitChoices;
    }

    public void setDataRaceKitChoices(String dataRaceKitChoices) {
        this.dataRaceKitChoices = dataRaceKitChoices;
    }


}

