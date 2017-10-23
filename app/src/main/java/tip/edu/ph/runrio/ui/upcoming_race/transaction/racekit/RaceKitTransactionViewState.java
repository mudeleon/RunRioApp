package tip.edu.ph.runrio.ui.upcoming_race.transaction.racekit;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;


public class RaceKitTransactionViewState implements RestorableViewState<RaceKitTransactionView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<RaceKitTransactionView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(RaceKitTransactionView view, boolean retained) {

    }
}
