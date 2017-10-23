package tip.edu.ph.runrio.ui.upcoming_race.transaction.racetype;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.app.Endpoints;
import tip.edu.ph.runrio.databinding.ItemRaceKitBinding;
import tip.edu.ph.runrio.databinding.ItemTransactionRaceTypeSingleBinding;
import tip.edu.ph.runrio.model.data.RaceType;


public class RaceTypeSingleAdapter extends RecyclerView.Adapter<RaceTypeSingleAdapter.ViewHolder> {
    private List<RaceType> event;
    private final Context context;
    private final RaceTypeTransactionView view;
    private static final int VIEW_TYPE_DEFAULT = 0;


    public RaceTypeSingleAdapter(Context context, RaceTypeTransactionView view) {
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
        ItemTransactionRaceTypeSingleBinding itemEventBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_transaction_race_type_single,
                parent,
                false);
        return new ViewHolder(itemEventBinding);
    }

    @Override
    public void onBindViewHolder(RaceTypeSingleAdapter.ViewHolder holder, int position) {
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

    public void setUpcomingRaces(List<RaceType> event) {
        this.event = event;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTransactionRaceTypeSingleBinding itemEventBinding;

        public ViewHolder(ItemTransactionRaceTypeSingleBinding itemEventBinding) {
            super(itemEventBinding.getRoot());
            this.itemEventBinding = itemEventBinding;
        }
    }
}
