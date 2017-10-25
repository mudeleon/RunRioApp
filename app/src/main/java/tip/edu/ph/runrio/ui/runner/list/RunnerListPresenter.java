package tip.edu.ph.runrio.ui.runner.list;

import android.util.Log;
import android.util.Patterns;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tip.edu.ph.runrio.app.App;
import tip.edu.ph.runrio.app.Constants;
import tip.edu.ph.runrio.model.data.CityList;
import tip.edu.ph.runrio.model.data.Country;
import tip.edu.ph.runrio.model.data.Profile;
import tip.edu.ph.runrio.model.data.Province;
import tip.edu.ph.runrio.model.response.BasicResponse;
import tip.edu.ph.runrio.util.DateTimeUtils;

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


    public void getProvince() {
        App.getInstance().getApiInterface().getProvince(Constants.APPJSON).enqueue(new Callback<List<Province>>() {
            @Override
            public void onResponse(Call<List<Province>> call, final Response<List<Province>> response) {
                if (response.isSuccessful()) {
                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.delete(Province.class);
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
                            error.printStackTrace();
                            getView().showError(error.getLocalizedMessage());
                        }
                    });
                } else {
                    getView().showError("Server Error");
                }
            }

            @Override
            public void onFailure(Call<List<Province>> call, Throwable t) {
                t.printStackTrace();
                getView().showError(t.getLocalizedMessage());
            }
        });
    }

    /**
     * Get Cities by its province
     *
     * @param province_id
     */
    public void getCities(Integer province_id) {
        App.getInstance().getApiInterface().getCities(province_id,Constants.APPJSON).enqueue(new Callback<List<CityList>>() {
            @Override
            public void onResponse(Call<List<CityList>> call, final Response<List<CityList>> response) {
                if (response.isSuccessful()) {
                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.delete(CityList.class);
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
                            error.printStackTrace();
                            getView().showError(error.getLocalizedMessage());
                        }
                    });
                } else {
                    getView().showError("Server Error");
                }
            }

            @Override
            public void onFailure(Call<List<CityList>> call, Throwable t) {
                t.printStackTrace();
                getView().showError(t.getLocalizedMessage());
            }
        });
    }


    public void getCountry() {
        App.getInstance().getApiInterface().getCountry(Constants.APPJSON).enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, final Response<List<Country>> response) {
                if (response.isSuccessful()) {
                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.delete(Country.class);
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
                            error.printStackTrace();
                            getView().showError(error.getLocalizedMessage());
                        }
                    });
                } else {
                    getView().showError("Server Error");
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                t.printStackTrace();
                getView().showError(t.getLocalizedMessage());
            }
        });
    }


    public void registerRunner(Map<String, String> args,String apiToken) {
        if (isMapEmpty(args)) { // check if all fields are field up
            getView().showError("Please fill-up all fields.");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(args.get("email")).matches()) { //check if email is valid
            getView().showError("Invalid email address.");
        } else { // process the registration
            getView().showProgress();
            Calendar calendarBDay = DateTimeUtils.convertTransactionStringDate(args.get("bday"), DateTimeUtils.DATE_ONLY);
            String stringBDay = DateTimeUtils.convertDateToString("yyyy-MM-dd", calendarBDay);
            args.put("bday", stringBDay);
            register(App.getInstance().getApiInterface().registerRunner(Constants.BEARER+apiToken,args,Constants.APPJSON));
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
                        getView().onSuccess(/*response.body().getMessage()*/);
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


    public void editRunner(Map<String, String> args, String id,String apiToken) {
        if (isMapEmpty(args)) { // check if all fields are field up
            getView().showError("Please fill-up all fields.");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(args.get("email")).matches()) { //check if email is valid
            getView().showError("Invalid email address.");
        } else { // process the registration
            getView().showProgress();
            Calendar calendarBDay = DateTimeUtils.convertTransactionStringDate(args.get("bday"), DateTimeUtils.DATE_ONLY);
            String stringBDay = DateTimeUtils.convertDateToString("yyyy-MM-dd", calendarBDay);
            args.put("bday", stringBDay);
            editRunnerSend(App.getInstance().getApiInterface().editRunner(id,Constants.BEARER+apiToken,args,Constants.APPJSON));
        }

    }


    private void editRunnerSend(Call<BasicResponse> registerCall) {
        getView().showProgress();
        registerCall.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                getView().dismissProgress();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        getView().onSuccess(/*response.body().getMessage()*/);
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

    public void deleteRunner(String id,String apiToken) {
        getView().showProgress();
            App.getInstance().getApiInterface().deleteRunner(Constants.BEARER+apiToken,id ,Constants.APPJSON)
                    .enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                getView().dismissProgress();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        getView().onSuccess(/*response.body().getMessage()*/);
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


}
