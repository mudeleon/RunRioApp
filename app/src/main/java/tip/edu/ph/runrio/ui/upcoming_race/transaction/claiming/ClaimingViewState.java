package tip.edu.ph.runrio.ui.upcoming_race.transaction.claiming;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;


public class ClaimingViewState implements RestorableViewState<ClaimingView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<ClaimingView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(ClaimingView view, boolean retained) {

    }
}
