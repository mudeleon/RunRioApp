package tip.edu.ph.runrio.ui.upcoming_race.transaction.expense;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;


public class ExpenseViewState implements RestorableViewState<ExpenseView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<ExpenseView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(ExpenseView view, boolean retained) {

    }
}
