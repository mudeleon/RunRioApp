package tip.edu.ph.runrio.ui.runner.list;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;


public class RunnerListViewState implements RestorableViewState<RunnerListView> {
    private static final String KEY_SEARCH = "key_search_text";
    private String searchText;

    @Override
    public void saveInstanceState(@NonNull Bundle out) {
        out.putString(KEY_SEARCH, searchText);
    }

    @Override
    public RestorableViewState<RunnerListView> restoreInstanceState(Bundle in) {
        searchText = in.getString(KEY_SEARCH, "");
        return this;
    }

    @Override
    public void apply(RunnerListView view, boolean retained) {
        view.prepareList();
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
