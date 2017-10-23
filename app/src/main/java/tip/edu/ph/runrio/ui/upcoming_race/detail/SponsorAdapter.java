package tip.edu.ph.runrio.ui.upcoming_race.detail;

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
import tip.edu.ph.runrio.databinding.ItemSponsorsBinding;
import tip.edu.ph.runrio.model.data.Sponsors;


public class SponsorAdapter extends RecyclerView.Adapter<SponsorAdapter.ViewHolder> {
    private List<Sponsors> event;
    private final Context context;
    private final UpcomingRaceDetailView view;
    private static final int VIEW_TYPE_DEFAULT = 0;


    public SponsorAdapter(Context context, UpcomingRaceDetailView view) {
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
        ItemSponsorsBinding itemEventBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_sponsors,
                parent,
                false);
        return new ViewHolder(itemEventBinding);
    }

    @Override
    public void onBindViewHolder(SponsorAdapter.ViewHolder holder, int position) {
        holder.itemEventBinding.setSponsor(event.get(position));
        holder.itemEventBinding.setView(view);
        Glide.with(holder.itemView.getContext())
                .load(Endpoints.EVENT_URL_IMAGE+event.get(position).getSponsorsImage())
                .centerCrop()
                .into(holder.itemEventBinding.upcomingRacesDetailSponsorImage);
    }


    public void clear() {
        event.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return event.size();
    }

    public void setUpcomingRaces(List<Sponsors> event) {
        this.event = event;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemSponsorsBinding itemEventBinding;

        public ViewHolder(ItemSponsorsBinding itemEventBinding) {
            super(itemEventBinding.getRoot());
            this.itemEventBinding = itemEventBinding;
        }
    }
}
