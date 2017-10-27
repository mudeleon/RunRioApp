package tip.edu.ph.runrio.ui.upcoming_race.transaction.racetype;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tip.edu.ph.runrio.app.App;
import tip.edu.ph.runrio.app.Constants;
import tip.edu.ph.runrio.model.data.Profile;
import tip.edu.ph.runrio.model.data.UpcomingRaces;
import tip.edu.ph.runrio.ui.upcoming_race.detail.UpcomingRaceDetailView;



public class RaceTypeTransactionPresenter extends MvpBasePresenter<RaceTypeTransactionView> {

    public void loadUpcomingRaces(String apiToken, String id) {

        App.getInstance().getApiInterface().getUpcomingRaceDetail(Constants.BEARER+apiToken, id,Constants.APPJSON)
                .enqueue(new Callback<UpcomingRaces>() {
                    @Override
                    public void onResponse(Call<UpcomingRaces> call, final Response<UpcomingRaces> response) {
                        if (isViewAttached()) {
                            getView().stopRefresh();
                        }
                        if (response.isSuccessful()) {
                            final Realm realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(UpcomingRaces.class);
                                    realm.copyToRealmOrUpdate(response.body());


                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    realm.close();
                                    getView().setUpcomingRaces();
                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    realm.close();
                                    error.printStackTrace();
                                    if (isViewAttached())
                                        getView().showError(error.getLocalizedMessage());
                                }
                            });
                        } else {
                            if (isViewAttached())
                                getView().showError(response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<UpcomingRaces> call, Throwable t) {
                        t.printStackTrace();
                        if (isViewAttached()) {
                            getView().stopRefresh();
                            getView().showError(t.getLocalizedMessage());
                        }
                    }
                });
    }


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
                                    //getView().setRunnerList();
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
