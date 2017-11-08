package tip.edu.ph.runrio.ui.upcoming_race.transaction.expense;

import android.util.Patterns;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.Calendar;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tip.edu.ph.runrio.app.App;
import tip.edu.ph.runrio.app.Constants;
import tip.edu.ph.runrio.model.response.BasicResponse;
import tip.edu.ph.runrio.util.DateTimeUtils;


/**
 * @author pocholomiaextends MvpBasePresenter<EventListView>
 * @since 03/10/2016
 */

public class ExpensePresenter extends MvpBasePresenter<ExpenseView> {



    public void registerRunner(Map<String, String> args, String apiToken) {
        if (isMapEmpty(args)) { // check if all fields are field up
            getView().showError("Please fill-up all fields.");
        } else { // process the registration
            getView().showProgress();
            register(App.getInstance().getApiInterface().reservation(Constants.BEARER+apiToken,args,Constants.APPJSON));
        }

    }


    private void register(Call<BasicResponse> registerCall) {
        getView().showProgress();
        registerCall.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                getView().dismissProgress();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        getView().onReserveSuccess(/*response.body().getMessage()*/);
                    } else {
                        getView().showError(response.body().getMessage());
                    }
                } else {
                    getView().showError("Server Error");
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                getView().dismissProgress();
                t.getLocalizedMessage();
                getView().showError(t.getLocalizedMessage());
            }
        });
    }


    private static <K, V> boolean isMapEmpty(Map<K, V> aMap) {
        for (V v : aMap.values()) {
            if (v == "") {
                return true;
            }
        }
        return false;
    }





}
