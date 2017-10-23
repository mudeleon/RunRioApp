package tip.edu.ph.runrio.ui.login;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tip.edu.ph.runrio.app.App;
import tip.edu.ph.runrio.model.response.LoginResponse;

public class LoginPresenter extends MvpNullObjectBasePresenter<LoginView> {

    /**
     * Try to login
     *
     * @param email    user email address
     * @param password user password
     */
    public void onLogin(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            getView().showToast("Fill-up all fields");
        } else {
            login(App.getInstance().getApiInterface().login(email, password));
        }
    }


    private void login(Call<LoginResponse> loginResponseCall) {
        getView().showLoad();
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                getView().dismissLoad();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        final Realm realm = Realm.getDefaultInstance();
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                               Log.d("LOGIN", "execute: user image: " + response.body().getUser().getImage());
                                 realm.copyToRealmOrUpdate(response.body().getUser());

                            }
                        }, new Realm.Transaction.OnSuccess() {
                            @Override
                            public void onSuccess() {
                                realm.close();
                                getView().startNewActivity();
                            }
                        }, new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                                realm.close();
                                error.printStackTrace();
                                getView().showToast(error.getLocalizedMessage());
                            }
                        });
                    } else {
                        getView().showToast(response.body().getMessage());
                    }
                } else {
                    getView().showToast("Server Error");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
                getView().dismissLoad();
                getView().showToast(t.getLocalizedMessage());
            }
        });



    }
}
