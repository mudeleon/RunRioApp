package tip.edu.ph.runrio.app;


public class Endpoints {



    //public static final String BASE_URL = "http://10.3.32.201/runrio";
    public static final String BASE_URL = "https://scheduler.dgts.ph/runrio";


    static final String API_URL = BASE_URL + "/api/";

    public static final String ID = "{id}";


    //Credentials
    public static final String LOGIN = "user/login";
    public static final String REGISTER = "user";
    public static final String CHANGE_PASSWORD = "user/changepass";
    public static final String UPDATE_ACCOUNT = "user/updateacct";
    public static final String FORGOT_PASSWORD = "user/forgotpass/{email_address}";
    public static final String LOGOUT = "user/logout";



    //Profile
    public static final String IMG_HOLDER = "{img}";
    public static final String IMAGE_URL = BASE_URL+"/storage/app/" + IMG_HOLDER;
    public static final String IMAGE_URL_FB = "http://graph.facebook.com/" + IMG_HOLDER + "/picture?type=large";
    public static final String PROVINCE = "province";
    public static final String COUNTRY = "country";
    public static final String CITIES = "cities/{province_id}";
    public static final String SECURITY_QUESTION = "security-question";
    public static final String SECURITY_QUESTION2 = "security-question";
    public static final String UPLOAD_IMG = "user/updateimage";



    //Company
    public static final String COMPANY_LIST = "company";
    public static final String COMPANY_DETAIL = COMPANY_LIST + "/" + ID;

    //Events
    public static final String EVENT_URL_IMAGE = BASE_URL + "/storage/app/";
    public static final String EVENT_LIST = "event";
    public static final String EVENT_DETAIL = "event" + "/" + ID;




     //Profile
     public static final String RUNNER_LIST = "participant_profile";
     public static final String RUNNER_DETAIL = RUNNER_LIST + "/" + ID;
     public static final String RUNNER_DELETE = RUNNER_LIST + "/change_status/" + ID;


     //transaction
     public static final String TRANSACTIONS = "reservations";
     public static final String TRANSACTIONS_DETAIL = TRANSACTIONS + "/" + ID;




}
