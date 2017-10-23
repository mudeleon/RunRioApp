package tip.edu.ph.runrio.ui.register;

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
import tip.edu.ph.runrio.model.data.Province;
import tip.edu.ph.runrio.model.data.SecurityQuestion;
import tip.edu.ph.runrio.model.data.SecurityQuestion2;
import tip.edu.ph.runrio.model.response.BasicResponse;
import tip.edu.ph.runrio.util.DateTimeUtils;


public class RegisterPresenter extends MvpNullObjectBasePresenter<RegisterView> {

//    /**
//     * Validate the registration
//     *
//     * @param firstName
//     * @param lastName
//     * @param emailAddress
//     * @param password
//     * @param repeatPassword
//     * @param address
//     * @param birthday
//     * @param mobileNumber
//     */
//    public void registerNormal(String firstName, String lastName, String emailAddress, String password,
//                               String repeatPassword, String address, String birthday, String mobileNumber) {
//        if (firstName.isEmpty() || lastName.isEmpty() || emailAddress.isEmpty() || password.isEmpty()
//                || repeatPassword.isEmpty() || address.isEmpty() || birthday.isEmpty() || mobileNumber.isEmpty()) {
//            getView().showAlert("Please fill-up all fields");
//        } else if (!password.contentEquals(repeatPassword)) {
//            getView().showAlert("Passwords does not match");
//        } else {
//            getView().showProgress();
//            Calendar calendarBDay = DateTimeUtils.convertTransactionStringDate(birthday, DateTimeUtils.DATE_ONLY);
//            String stringBDay = DateTimeUtils.convertDateToString("yyyy-MM-dd", calendarBDay);
//            register(App.getInstance().getApiInterface().register(emailAddress, firstName, lastName, password, address, stringBDay, mobileNumber));
//        }
//    }

    /**
     * Process the registration
     *
     * @param args - arguments
     */
    public void register(Map<String, String> args) {
        if (isMapEmpty(args)) { // check if all fields are field up
            getView().showAlert("Please fill-up all fields.");
        } else if (!args.get("password").contentEquals(args.get("password_confirmation"))) { //check if password matches
            getView().showAlert("Passwords does not match.");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(args.get("email")).matches()) { //check if email is valid
            getView().showAlert("Invalid email address.");
        } else if (!Patterns.PHONE.matcher(args.get("contact_prefix")+args.get("contact_no")).matches()) { // check if mobile number is valid
            getView().showAlert("Invalid mobile number.");
        } else if (args.get("security_question_1").contentEquals("Select Security Question") || args.get("security_question_2").contentEquals("Select Security Question")) { // check if the user selected security question
            getView().showAlert("Please select security question.");
        } else { // process the registration
            getView().showProgress();
            Calendar calendarBDay = DateTimeUtils.convertTransactionStringDate(args.get("bday"), DateTimeUtils.DATE_ONLY);
            String stringBDay = DateTimeUtils.convertDateToString("yyyy-MM-dd", calendarBDay);
            args.put("bday", stringBDay);
            register(App.getInstance().getApiInterface().register(args));
        }

    }


    //Get Country

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
                            getView().showAlert(error.getLocalizedMessage());
                        }
                    });
                } else {
                    getView().showAlert("Server Error");
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
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
     * Get security question
     *
     */
    public void getSQ1() {
        App.getInstance().getApiInterface().getSecurityQuestions(1,Constants.APPJSON).enqueue(new Callback<List<SecurityQuestion>>() {
            @Override
            public void onResponse(Call<List<SecurityQuestion>> call, final Response<List<SecurityQuestion>> response) {
                if (response.isSuccessful()) {
                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.delete(SecurityQuestion.class);
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
            public void onFailure(Call<List<SecurityQuestion>> call, Throwable t) {
                t.printStackTrace();
                getView().showAlert(t.getLocalizedMessage());
            }
        });
    }

    /**
     * Get security question
     *
     */
    public void getSQ2() {
        App.getInstance().getApiInterface().getSecurityQuestions2(2,Constants.APPJSON).enqueue(new Callback<List<SecurityQuestion2>>() {

            @Override
            public void onResponse(Call<List<SecurityQuestion2>> call, final Response<List<SecurityQuestion2>> response) {
                if (response.isSuccessful()) {
                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.delete(SecurityQuestion2.class);
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
            public void onFailure(Call<List<SecurityQuestion2>> call, Throwable t) {
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


    /**
     * Register action
     *
     * @param registerCall
     */
    private void register(Call<BasicResponse> registerCall) {
        getView().showProgress();
        registerCall.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                getView().dismissProgress();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        getView().registerSuccessful(response.body().getMessage());
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
                t.getLocalizedMessage();
                getView().showAlert(t.getLocalizedMessage());
            }
        });
    }

    /**
     * Check if all values are null
     *
     * @param aMap - args hash map
     * @param <K>  - key
     * @param <V>  - value
     * @return
     */
    private static <K, V> boolean isMapEmpty(Map<K, V> aMap) {
        for (V v : aMap.values()) {
            if (v == "") {
                return true;
            }
        }
        return false;
    }

}
