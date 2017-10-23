package tip.edu.ph.runrio.ui.upcoming_race.transaction.racetype;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;




public class RaceTypeTransactionViewState implements RestorableViewState<RaceTypeTransactionView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<RaceTypeTransactionView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(RaceTypeTransactionView view, boolean retained) {

    }
}
