package tip.edu.ph.runrio.ui.runner.detail;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tip.edu.ph.runrio.app.App;
import tip.edu.ph.runrio.app.Constants;
import tip.edu.ph.runrio.model.data.Profile;

public class RunnerDetailPresenter extends MvpNullObjectBasePresenter<RunnerDetailView> {

    public void getRunnerDetails(String apiToken, String adminId) {
        getView().showProgress();
        App.getInstance().getApiInterface().getProfileDetail(Constants.BEARER+apiToken, adminId, Constants.APPJSON)
                .enqueue(new Callback<Profile>() {
                    @Override
                    public void onResponse(Call<Profile> call, final Response<Profile> response) {
                        getView().hideProgress();
                        if (response.isSuccessful()) {
                            final Realm realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealmOrUpdate(response.body());
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    realm.close();
                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    realm.close();
                                    getView().showAlert(error.getLocalizedMessage());
                                }
                            });
                        } else {
                            getView().showAlert("Server Error");
                        }
                    }

                    @Override
                    public void onFailure(Call<Profile> call, Throwable t) {
                        getView().hideProgress();
                        t.printStackTrace();
                        getView().showAlert(t.getLocalizedMessage());
                    }
                });
    }


}
