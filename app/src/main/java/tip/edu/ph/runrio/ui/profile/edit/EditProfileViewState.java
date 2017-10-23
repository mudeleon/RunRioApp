package tip.edu.ph.runrio.ui.profile.edit;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * @author pocholomia
 * @since 02/11/2016
 */

class EditProfileViewState implements RestorableViewState<EditProfileView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<EditProfileView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(EditProfileView view, boolean retained) {

    }
}
