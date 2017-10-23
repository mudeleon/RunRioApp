package tip.edu.ph.runrio.model.response;


import com.google.gson.annotations.SerializedName;

import tip.edu.ph.runrio.app.Constants;
import tip.edu.ph.runrio.model.data.User;


public class LoginResponse extends BasicResponse {

    @SerializedName(Constants.DATA)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
