package tip.edu.ph.runrio.model.response;

import com.google.gson.annotations.SerializedName;



public class UploadProfileImageResponse extends BasicResponse {

    @SerializedName("data")
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
