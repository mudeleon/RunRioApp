package tip.edu.ph.runrio.ui.forgot_password;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;



public class ForgotPasswordViewState implements RestorableViewState<ForgotPasswordView> {
    private static final String KEY_EMAIL = "key_email_address";
    private String email;

    @Override
    public void saveInstanceState(@NonNull Bundle out) {
        out.putString(KEY_EMAIL, email);
    }

    @Override
    public RestorableViewState<ForgotPasswordView> restoreInstanceState(Bundle in) {
        email = in.getString(KEY_EMAIL, "");
        return this;
    }

    @Override
    public void apply(ForgotPasswordView view, boolean retained) {
        view.setEmailText(email);
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
