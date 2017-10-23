package tip.edu.ph.runrio.ui.login;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * @author pocholomia
 * @since 08/11/2016
 */

public class LoginViewState implements RestorableViewState<LoginView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<LoginView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(LoginView view, boolean retained) {

    }
}
