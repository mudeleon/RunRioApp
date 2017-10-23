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
import tip.edu.ph.runrio.databinding.ItemClaimingUpcomingDetailBinding;
import tip.edu.ph.runrio.model.data.ClaimingType;



public class ClaimingRateAdapter extends RecyclerView.Adapter<ClaimingRateAdapter.ViewHolder> {
    private List<ClaimingType> event;
    private final Context context;
    private final UpcomingRaceDetailView view;
    private static final int VIEW_TYPE_DEFAULT = 0;


    public ClaimingRateAdapter(Context context, UpcomingRaceDetailView view) {
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
        ItemClaimingUpcomingDetailBinding itemEventBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_claiming_upcoming_detail,
                parent,
                false);
        return new ViewHolder(itemEventBinding);
    }

    @Override
    public void onBindViewHolder(ClaimingRateAdapter.ViewHolder holder, int position) {
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
        private final ItemClaimingUpcomingDetailBinding itemEventBinding;

        public ViewHolder(ItemClaimingUpcomingDetailBinding itemEventBinding) {
            super(itemEventBinding.getRoot());
            this.itemEventBinding = itemEventBinding;
        }
    }
}
