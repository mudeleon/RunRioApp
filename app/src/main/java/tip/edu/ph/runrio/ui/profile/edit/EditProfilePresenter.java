package tip.edu.ph.runrio.ui.profile.edit;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tip.edu.ph.runrio.app.App;
import tip.edu.ph.runrio.app.Constants;
import tip.edu.ph.runrio.model.data.CityList;
import tip.edu.ph.runrio.model.data.Province;
import tip.edu.ph.runrio.model.data.User;
import tip.edu.ph.runrio.model.response.LoginResponse;
import tip.edu.ph.runrio.model.response.UploadProfileImageResponse;
import tip.edu.ph.runrio.util.DateTimeUtils;
import tip.edu.ph.runrio.util.MapNullChecker;


/**
 * @author pocholomia
 * @since 02/11/2016
 */
public class EditProfilePresenter extends MvpNullObjectBasePresenter<EditProfileView> {

    private static final String TAG = EditProfileActivity.class.getSimpleName();

    public void updateProfile(String apiToken, Map<String, String> args) {
        if (MapNullChecker.isMapEmpty(args)) {
            getView().showAlert("Please fill-up all fields.");
        } else {
            getView().showLoading();
            Calendar calendarBDay = DateTimeUtils.convertTransactionStringDate(args.get("bday"), DateTimeUtils.DATE_ONLY);
            String stringBDay = DateTimeUtils.convertDateToString("yyyy-MM-dd", calendarBDay);
            args.put("bday", stringBDay);
            App.getInstance().getApiInterface().updateAccount(Constants.BEARER+apiToken, args,Constants.APPJSON)
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                            getView().stopLoading();
                            if (response.isSuccessful()) {
                                if (response.body().isSuccess()) {
                                    final Realm realm = Realm.getDefaultInstance();
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            realm.copyToRealmOrUpdate(response.body().getUser());
                                        }
                                    }, new Realm.Transaction.OnSuccess() {
                                        @Override
                                        public void onSuccess() {
                                            realm.close();
                                            getView().updateAccountSuccessful(response.body().getMessage());
                                        }
                                    }, new Realm.Transaction.OnError() {
                                        @Override
                                        public void onError(Throwable error) {
                                            realm.close();
                                            Log.e(TAG, "Realm Error Saving User Info", error);
                                            getView().showAlert(error.getLocalizedMessage());
                                        }
                                    });
                                } else {
                                    getView().showAlert(response.body().getMessage());
                                }
                            } else {
                                getView().showAlert("Server Error!");
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            getView().stopLoading();
                            t.getLocalizedMessage();
                            getView().showAlert("Unable to Send Request to Server");
                        }
                    });
        }
    }

//    public void updateProfile(String apiToken, String firstName, String lastName, String address, String birthday, String cpNumber) {
//        if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || birthday.isEmpty() || cpNumber.isEmpty()) {
//            getView().showAlert("Please fill-up all fields");
//        } else {
//            getView().showLoading();
//            Calendar calendarBDay = DateTimeUtils.convertTransactionStringDate(birthday, DateTimeUtils.DATE_ONLY);
//            String stringBDay = DateTimeUtils.convertDateToString("yyyy-MM-dd", calendarBDay);
//            App.getInstance().getApiInterface().updateAccount(apiToken, firstName, lastName, address, stringBDay, cpNumber)
//                    .enqueue(new Callback<LoginResponse>() {
//                        @Override
//                        public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
//                            getView().stopLoading();
//                            if (response.isSuccessful()) {
//                                if (response.body().isSuccess()) {
//                                    final Realm realm = Realm.getDefaultInstance();
//                                    realm.executeTransactionAsync(new Realm.Transaction() {
//                                        @Override
//                                        public void execute(Realm realm) {
//                                            realm.copyToRealmOrUpdate(response.body().getUser());
//                                        }
//                                    }, new Realm.Transaction.OnSuccess() {
//                                        @Override
//                                        public void onSuccess() {
//                                            realm.close();
//                                            getView().updateAccountSuccessful(response.body().getMessage());
//                                        }
//                                    }, new Realm.Transaction.OnError() {
//                                        @Override
//                                        public void onError(Throwable error) {
//                                            realm.close();
//                                            Log.e(TAG, "Realm Error Saving User Info", error);
//                                            getView().showAlert(error.getLocalizedMessage());
//                                        }
//                                    });
//                                } else {
//                                    getView().showAlert(response.body().getMessage());
//                                }
//                            } else {
//                                getView().showAlert("Server Error!");
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<LoginResponse> call, Throwable t) {
//                            getView().stopLoading();
//                            t.getLocalizedMessage();
//                            getView().showAlert("Unable to Send Request to Server");
//                        }
//                    });
//        }
//    }

    public void upload(String apiToken, final File imageFile) {
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);
        getView().showLoading();
        App.getInstance().getApiInterface().upload(Constants.BEARER+apiToken,Constants.APPJSON ,body)
                .enqueue(new Callback<UploadProfileImageResponse>() {
                    @Override
                    public void onResponse(Call<UploadProfileImageResponse> call, final Response<UploadProfileImageResponse> response) {
                        getView().stopLoading();
                        if (response.isSuccessful()) {
                            if (response.body().isSuccess()) {
                                final Realm realm = Realm.getDefaultInstance();
                                realm.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        User user = realm.where(User.class).findFirst();
                                        user.setImage(response.body().getImage());
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
                                        getView().showAlert(error.getLocalizedMessage());
                                    }
                                });
                            } else {
                                getView().showAlert(response.body().getMessage());
                            }
                        } else {
                            getView().showAlert("Error Server Connection");
                        }
                    }

                    @Override
                    public void onFailure(Call<UploadProfileImageResponse> call, Throwable t) {
                        getView().stopLoading();
                        t.printStackTrace();
                        getView().showAlert(t.getLocalizedMessage());
                    }
                });
    }

    /**
     * Get all province available in API
     */
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
                            getView().showAlert(error.getLocalizedMessage());
                        }
                    });
                } else {
                    getView().showAlert("Server Error");
                }
            }

            @Override
            public void onFailure(Call<List<Province>> call, Throwable t) {
                t.printStackTrace();
                getView().showAlert(t.getLocalizedMessage());
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
                            getView().showAlert(error.getLocalizedMessage());
                        }
                    });
                } else {
                    getView().showAlert("Server Error");
                }
            }

            @Override
            public void onFailure(Call<List<CityList>> call, Throwable t) {
                t.printStackTrace();
                getView().showAlert(t.getLocalizedMessage());
            }
        });
    }

}
