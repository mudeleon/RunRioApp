package tip.edu.ph.runrio.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * @author pocholomia
 * @since 05/10/2016
 */

class RacesListViewState implements RestorableViewState<RacesListView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<RacesListView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(RacesListView view, boolean retained) {

    }
}
