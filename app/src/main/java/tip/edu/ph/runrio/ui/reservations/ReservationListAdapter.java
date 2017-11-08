package tip.edu.ph.runrio.ui.reservations;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.databinding.ItemReservationBinding;
import tip.edu.ph.runrio.model.data.Reservation;


public class ReservationListAdapter extends RecyclerView.Adapter<ReservationListAdapter.ViewHolder> {
    private List<Reservation> list;
    private static final int VIEW_TYPE_DEFAULT = 0;
    private ReservationsView view;

    public ReservationListAdapter(ReservationsView view) {
        this.view = view;
        list = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemReservationBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_reservation,
                parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Reservation item = list.get(position);
        holder.binding.setRes(item);
        holder.binding.setView(view);
        holder.binding.persons.setText(item.getReservationRunnerReservationCategory().size()+" persons");


        //Glide.with(holder.itemView.getContext()).load(Endpoints.IMAGE_URL+item.getReservedByDetails().getUserImage()).centerCrop().into(holder.binding.imageView);


    }


    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<Reservation> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemReservationBinding binding;

        public ViewHolder(ItemReservationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
