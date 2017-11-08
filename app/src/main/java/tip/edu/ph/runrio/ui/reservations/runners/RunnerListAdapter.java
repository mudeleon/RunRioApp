package tip.edu.ph.runrio.ui.reservations.runners;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.databinding.ItemReservationRunnerBinding;
import tip.edu.ph.runrio.model.data.RunnerReservation;
import tip.edu.ph.runrio.util.DateTimeUtils;


public class RunnerListAdapter extends RecyclerView.Adapter<RunnerListAdapter.ViewHolder> {
    private List<RunnerReservation> list;
    private static final int VIEW_TYPE_DEFAULT = 0;
    private RunnerListView view;

    public RunnerListAdapter(RunnerListView view) {
        this.view = view;
        list = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemReservationRunnerBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_reservation_runner,
                parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RunnerReservation item = list.get(position);
        holder.binding.setRunnerRes(item);
        holder.binding.setView(view);


        holder.binding.runnerFullName2.setText(list.get(position).getProfileCategory().getFullName());
        holder.binding.runnerGender2.setText(DateTimeUtils.getGender(list.get(position).getProfileCategory().getProfileGender()));
        holder.binding.runnerRaceType2.setText(list.get(position).getRacetypeCategory().getRaceTypeName());

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
        private final ItemReservationRunnerBinding binding;

        public ViewHolder(ItemReservationRunnerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
