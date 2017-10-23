package tip.edu.ph.runrio.ui.forgot_password;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tip.edu.ph.runrio.app.App;
import tip.edu.ph.runrio.model.response.BasicResponse;

public class ForgotPasswordPresenter extends MvpNullObjectBasePresenter<ForgotPasswordView> {

    public void forgotPassword(String emailAddress) {
        if (emailAddress.isEmpty()) {
            getView().showAlert("Enter Email Address");
        } else {
            getView().showProgress();
            App.getInstance().getApiInterface().forgotPassword(emailAddress)
                    .enqueue(new Callback<BasicResponse>() {
                        @Override
                        public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                            getView().dismissProgress();
                            if (response.isSuccessful()) {
                                if (response.body().isSuccess()) {
                                    getView().forgotPasswordSuccessful(response.body().getMessage());
                                } else {
                                    getView().showAlert(response.body().getMessage());
                                }
                            } else {
                                getView().showAlert("Server Error");
                            }
                        }

                        @Override
                        public void onFailure(Call<BasicResponse> call, Throwable t) {
                            getView().dismissProgress();
                            t.printStackTrace();
                            getView().showAlert(t.getLocalizedMessage());
                        }
                    });

        }
    }
}
