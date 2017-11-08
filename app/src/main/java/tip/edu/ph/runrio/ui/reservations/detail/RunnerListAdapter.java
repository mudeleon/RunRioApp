package tip.edu.ph.runrio.ui.reservations.detail;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.databinding.ItemReservationDetailRunnerBinding;
import tip.edu.ph.runrio.model.data.RunnerReservation;
import tip.edu.ph.runrio.util.DateTimeUtils;


public class RunnerListAdapter extends RecyclerView.Adapter<RunnerListAdapter.ViewHolder> {
    private List<RunnerReservation> list;
    private static final int VIEW_TYPE_DEFAULT = 0;
    private ReservationDetailView view;

    public RunnerListAdapter(ReservationDetailView view) {
        this.view = view;
        list = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemReservationDetailRunnerBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_reservation_detail_runner,
                parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RunnerReservation item = list.get(position);
        holder.binding.setRunnerRes(item);
        holder.binding.setView(view);


        holder.binding.runnerFullName.setText(list.get(position).getProfileCategory().getFullName());
        holder.binding.runnerGender.setText(DateTimeUtils.getGender(list.get(position).getProfileCategory().getProfileGender()));
        holder.binding.runnerRaceType.setText(list.get(position).getRacetypeCategory().getRaceTypeName());



    }


    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<RunnerReservation> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemReservationDetailRunnerBinding binding;

        public ViewHolder(ItemReservationDetailRunnerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
