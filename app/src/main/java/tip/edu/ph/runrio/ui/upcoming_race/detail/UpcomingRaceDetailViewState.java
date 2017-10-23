package tip.edu.ph.runrio.ui.upcoming_race.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;


public class UpcomingRaceDetailViewState implements RestorableViewState<UpcomingRaceDetailView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<UpcomingRaceDetailView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(UpcomingRaceDetailView view, boolean retained) {

    }
}
