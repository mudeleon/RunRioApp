package tip.edu.ph.runrio.ui.main.list;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.databinding.ItemRaceResultBinding;
import tip.edu.ph.runrio.databinding.ItemRaceResultListBinding;
import tip.edu.ph.runrio.model.data.RacesResult;
import tip.edu.ph.runrio.ui.main.RacesListView;


public class RaceResultListAdapter extends RecyclerView.Adapter<RaceResultListAdapter.ViewHolder> {
    private List<RacesResult> event;
    private final Context context;
    private final EventListView view;
    private static final int VIEW_TYPE_DEFAULT = 0;


    public RaceResultListAdapter(Context context, EventListView view) {
        this.context = context;
        this.view = view;
        event = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemRaceResultListBinding itemEventBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_race_result_list,
                parent,
                false);
        return new ViewHolder(itemEventBinding);
    }

    @Override
    public void onBindViewHolder(RaceResultListAdapter.ViewHolder holder, int position) {
        holder.itemEventBinding.setRace(event.get(position));
        holder.itemEventBinding.setView(view);
    }


    public void clear() {
        event.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return event.size();
    }

    public void setRacesResult(List<RacesResult> event) {
        this.event = event;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemRaceResultListBinding itemEventBinding;

        public ViewHolder(ItemRaceResultListBinding itemEventBinding) {
            super(itemEventBinding.getRoot());
            this.itemEventBinding = itemEventBinding;
        }
    }
}
