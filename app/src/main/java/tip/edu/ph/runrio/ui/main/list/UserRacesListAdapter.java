package tip.edu.ph.runrio.ui.main.list;

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
import tip.edu.ph.runrio.databinding.ItemUserRaceHomeBinding;
import tip.edu.ph.runrio.databinding.ItemUserRaceHomeListBinding;
import tip.edu.ph.runrio.model.data.Reservation;
import tip.edu.ph.runrio.ui.main.RacesListView;


public class UserRacesListAdapter extends RecyclerView.Adapter<UserRacesListAdapter.ViewHolder> {
    private List<Reservation> event;
    private final Context context;
    private final EventListView view;
    private static final int VIEW_TYPE_DEFAULT = 0;


    public UserRacesListAdapter(Context context, EventListView view) {
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
        ItemUserRaceHomeListBinding itemEventBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_user_race_home_list,
                parent,
                false);
        return new ViewHolder(itemEventBinding);
    }

    @Override
    public void onBindViewHolder(UserRacesListAdapter.ViewHolder holder, int position) {
        holder.itemEventBinding.setRace(event.get(position));
        holder.itemEventBinding.setView(view);
        Glide.with(holder.itemView.getContext())
                .load(Endpoints.EVENT_URL_IMAGE+event.get(position).getReservationReservationInfoCategory().getReservationInfoImage())
                .centerCrop()
                // .error(R.drawable.sample_event)
                .into(holder.itemEventBinding.raceUserListImage);
    }


    public void clear() {
        event.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return event.size();
    }

    public void setUserRaces(List<Reservation> event) {
        this.event = event;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemUserRaceHomeListBinding itemEventBinding;

        public ViewHolder(ItemUserRaceHomeListBinding itemEventBinding) {
            super(itemEventBinding.getRoot());
            this.itemEventBinding = itemEventBinding;
        }
    }
}
