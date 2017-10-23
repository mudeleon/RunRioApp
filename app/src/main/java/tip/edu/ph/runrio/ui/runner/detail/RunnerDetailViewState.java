package tip.edu.ph.runrio.ui.runner.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;


public class RunnerDetailViewState implements RestorableViewState<RunnerDetailView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<RunnerDetailView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(RunnerDetailView view, boolean retained) {

    }
}
