package tip.edu.ph.runrio.ui.upcoming_race.transaction.claiming;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.databinding.ItemClaimingBinding;
import tip.edu.ph.runrio.databinding.ItemClaimingUpcomingDetailBinding;
import tip.edu.ph.runrio.model.data.ClaimingType;
import tip.edu.ph.runrio.ui.upcoming_race.detail.UpcomingRaceDetailView;


public class ClaimingListAdapter extends RecyclerView.Adapter<ClaimingListAdapter.ViewHolder> {
    private List<ClaimingType> event;
    private final Context context;
    private final ClaimingView view;
    private static final int VIEW_TYPE_DEFAULT = 0;


    public ClaimingListAdapter(Context context, ClaimingView view) {
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
        ItemClaimingBinding itemEventBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_claiming_,
                parent,
                false);
        return new ViewHolder(itemEventBinding);
    }

    @Override
    public void onBindViewHolder(ClaimingListAdapter.ViewHolder holder, int position) {
        holder.itemEventBinding.setRate(event.get(position));
        holder.itemEventBinding.setView(view);

        StringBuilder builder = new StringBuilder();
        if(event.get(position).getClaimingTypeType().equalsIgnoreCase("P"))
        {

            for (int i = 0; i < event.get(position).getDeliveryRateCategory().size(); i++) {
                builder.append(event.get(position).getDeliveryRateCategory().get(i).getDeliveryRateOne()).append("\n");
            }
            holder.itemEventBinding.claimingUpcomingDetailAddress.setText(builder.toString());
        }
        else
        {
            for (int i = 0; i < event.get(position).getDeliveryRateCategory().size(); i++) {
                builder.append(event.get(position).getDeliveryRateCategory().get(i).getDeliveryRateOne()).append(": Php ").append(event.get(position).getDeliveryRateCategory().get(i).getDeliveryRateTwo()).append(".00 \n");
            }
            holder.itemEventBinding.claimingUpcomingDetailAddress.setText(builder.toString());

        }

    }


    public void clear() {
        event.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return event.size();
    }

    public void setUpcomingRaces(List<ClaimingType> event) {
        this.event = event;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemClaimingBinding itemEventBinding;

        public ViewHolder(ItemClaimingBinding itemEventBinding) {
            super(itemEventBinding.getRoot());
            this.itemEventBinding = itemEventBinding;
        }
    }
}
