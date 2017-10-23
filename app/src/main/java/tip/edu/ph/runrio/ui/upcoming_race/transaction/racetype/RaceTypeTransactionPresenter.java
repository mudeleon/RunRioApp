package tip.edu.ph.runrio.ui.upcoming_race.transaction.racetype;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tip.edu.ph.runrio.app.App;
import tip.edu.ph.runrio.app.Constants;
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



}
