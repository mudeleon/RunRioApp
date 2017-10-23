package tip.edu.ph.runrio.ui.runner.list;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tip.edu.ph.runrio.app.App;
import tip.edu.ph.runrio.app.Constants;
import tip.edu.ph.runrio.model.data.Profile;

/**
 * @author pocholomia
 * @since 03/10/2016
 */

public class RunnerListPresenter extends MvpNullObjectBasePresenter<RunnerListView> {

    public void loadRunner(String apiToken) {

        App.getInstance().getApiInterface().getProfiles(Constants.BEARER+apiToken, Constants.APPJSON)
                .enqueue(new Callback<List<Profile>>() {
                    @Override
                    public void onResponse(Call<List<Profile>> call, final Response<List<Profile>> response) {
                        getView().stopRefresh();
                        if (response.isSuccessful()) {
                            final Realm realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(Profile.class);
                                    realm.copyToRealmOrUpdate(response.body());
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    realm.close();
                                    getView().setRunnerList();
                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    realm.close();
                                    error.printStackTrace();
                                    getView().showError("Error Parsing Data");
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Profile>> call, Throwable t) {
                        getView().stopRefresh();
                        t.printStackTrace();
                        getView().showError("Error Calling API");
                    }
                });


    }



    }
