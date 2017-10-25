package tip.edu.ph.runrio.ui.upcoming_race.transaction.profiletab.profilefragment;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tip.edu.ph.runrio.app.App;
import tip.edu.ph.runrio.model.response.BasicResponse;

public class ProfilePresenter extends MvpNullObjectBasePresenter<ProfileView> {
    private static final String TAG = ProfilePresenter.class.getSimpleName();

    public void changePassword(String apiToken, String emailAddress, String oldPassword, String newPassword) {
        getView().showProgress();
        App.getInstance().getApiInterface().changePassword(apiToken, emailAddress, oldPassword, newPassword)
                .enqueue(new Callback<BasicResponse>() {
                    @Override
                    public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                        getView().stopProgress();
                        if (response.isSuccessful()) {
                            getView().showAlert(response.body().getMessage());
                        } else {
                            getView().showAlert("Server Error");
                        }
                    }

                    @Override
                    public void onFailure(Call<BasicResponse> call, Throwable t) {
                        getView().stopProgress();
                        Log.e(TAG, "API Call Failure", t);
                        getView().showAlert("Error Calling API");
                    }
                });
    }
}
