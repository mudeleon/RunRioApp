package tip.edu.ph.runrio.app;



import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tip.edu.ph.runrio.model.data.CityList;
import tip.edu.ph.runrio.model.data.Country;
import tip.edu.ph.runrio.model.data.Profile;
import tip.edu.ph.runrio.model.data.Province;
import tip.edu.ph.runrio.model.data.Reservation;
import tip.edu.ph.runrio.model.data.SecurityQuestion;
import tip.edu.ph.runrio.model.data.SecurityQuestion2;
import tip.edu.ph.runrio.model.data.UpcomingRaces;
import tip.edu.ph.runrio.model.response.BasicResponse;
import tip.edu.ph.runrio.model.response.LoginResponse;
import tip.edu.ph.runrio.model.response.UploadProfileImageResponse;


public interface ApiInterface {


    //Credentials
    @FormUrlEncoded
    @POST(Endpoints.LOGIN)
    Call<LoginResponse> login(@Field(Constants.EMAIL_ADD) String emailAddress,
                              @Field(Constants.PASSWORD) String password);

    @GET(Endpoints.FORGOT_PASSWORD)
    Call<BasicResponse> forgotPassword(@Path("email_address") String email);

    @FormUrlEncoded
    @POST(Endpoints.REGISTER)
    Call<BasicResponse> register(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @PUT(Endpoints.CHANGE_PASSWORD)
    Call<BasicResponse> changePassword(@Header(Constants.AUTHORIZATION) String authorization,
                                       @Field(Constants.EMAIL_ADD) String emailAdd,
                                       @Field(Constants.OLD_PASSWORD) String oldPassword,
                                       @Field(Constants.PASSWORD) String password);

    @POST(Endpoints.LOGOUT)
    Call<BasicResponse> deleteGCMToken(@Header(Constants.AUTHORIZATION) String authorization, @Header(Constants.ACCEPT) String json);





    //Profile
    @GET(Endpoints.PROVINCE)
    Call<List<Province>> getProvince(@Header(Constants.ACCEPT) String json);

    @GET(Endpoints.COUNTRY)
    Call<List<Country>> getCountry(@Header(Constants.ACCEPT) String json);

    @GET(Endpoints.CITIES)
    Call<List<CityList>> getCities(@Path("province_id") Integer province_id, @Header(Constants.ACCEPT) String json);


    @GET(Endpoints.SECURITY_QUESTION)
    Call<List<SecurityQuestion>> getSecurityQuestions(@Query("set_number") Integer set_number, @Header(Constants.ACCEPT) String json);

    @GET(Endpoints.SECURITY_QUESTION2)
    Call<List<SecurityQuestion2>> getSecurityQuestions2(@Query("set_number") Integer set_number, @Header(Constants.ACCEPT) String json);

    @FormUrlEncoded
    @POST(Endpoints.UPDATE_ACCOUNT)
    Call<LoginResponse> updateAccount(@Header(Constants.AUTHORIZATION) String authorization,
                                      @FieldMap Map<String, String> params, @Header(Constants.ACCEPT) String json);

    @Multipart
    @POST(Endpoints.UPLOAD_IMG)
    Call<UploadProfileImageResponse> upload(@Header(Constants.AUTHORIZATION) String authorization, @Header(Constants.ACCEPT) String json
            , @Part MultipartBody.Part file);





    //Load Races

    @GET(Endpoints.EVENT_DETAIL)
    Call<UpcomingRaces>getUpcomingRaceDetail(@Header(Constants.AUTHORIZATION) String authorization,
                                      @Path("id") String eventId, @Header(Constants.ACCEPT) String json);

    @GET(Endpoints.EVENT_LIST)
    Call<List<UpcomingRaces>>getUpcomingRaces(@Header(Constants.AUTHORIZATION) String authorization ,@Header(Constants.ACCEPT) String json);



    @GET(Endpoints.TRANSACTIONS_DETAIL)
    Call<Reservation>getUserReservationDetail(@Header(Constants.AUTHORIZATION) String authorization,
                                     @Path("id") String eventId, @Header(Constants.ACCEPT) String json);

    @GET(Endpoints.TRANSACTIONS)
    Call<List<Reservation>>getUserReservations(@Header(Constants.AUTHORIZATION) String authorization ,@Header(Constants.ACCEPT) String json);


    @FormUrlEncoded
    @POST(Endpoints.TRANSACTIONS)
    Call<BasicResponse> reservation(@Header(Constants.AUTHORIZATION) String authorization,
                                       @FieldMap Map<String, String> params, @Header(Constants.ACCEPT) String json);


    //Profile

    @GET(Endpoints.RUNNER_DETAIL)
    Call<Profile>getProfileDetail(@Header(Constants.AUTHORIZATION) String authorization,
                                       @Path("id") String eventId, @Header(Constants.ACCEPT) String json);

    @GET(Endpoints.RUNNER_LIST)
    Call<List<Profile>>getProfiles(@Header(Constants.AUTHORIZATION) String authorization ,@Header(Constants.ACCEPT) String json);


    @FormUrlEncoded
    @POST(Endpoints.RUNNER_LIST)
    Call<BasicResponse> registerRunner(@Header(Constants.AUTHORIZATION) String authorization,
                                       @FieldMap Map<String, String> params, @Header(Constants.ACCEPT) String json);


    @FormUrlEncoded
    @PUT(Endpoints.RUNNER_DETAIL)
    Call<BasicResponse> editRunner(@Path("id") String eventId,@Header(Constants.AUTHORIZATION) String authorization,
                                       @FieldMap Map<String, String> params, @Header(Constants.ACCEPT) String json);

    @PUT(Endpoints.RUNNER_DELETE)
    Call<BasicResponse> deleteRunner(@Header(Constants.AUTHORIZATION) String authorization,
                                  @Path("id") String eventId, @Header(Constants.ACCEPT) String json);


}
