package tip.edu.ph.runrio.ui.main.list;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;


class EventListViewState implements RestorableViewState<EventListView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<EventListView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(EventListView view, boolean retained) {

    }
}
