package tip.edu.ph.runrio.ui.runner.list;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.databinding.ItemRunnerListBinding;
import tip.edu.ph.runrio.model.data.Profile;


public class RunnerListAdapter extends RecyclerView.Adapter<RunnerListAdapter.ViewHolder> {

    private final Context context;
    private final RunnerListView view;
    private List<Profile> adminList;

    public RunnerListAdapter(Context context, RunnerListView view) {
        this.context = context;
        this.view = view;
        adminList = new ArrayList<>();
    }

    public void setRunnerList(List<Profile> adminList) {
        this.adminList = adminList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemRunnerListBinding itemAdminBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_runner_list,
                parent,
                false);
        return new ViewHolder(itemAdminBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemAdminBinding.setRunner(adminList.get(position));
        holder.itemAdminBinding.setView(view);

        holder.itemAdminBinding.runnerListNumber.setText("Runner # "+(position+1));

    }

    @Override
    public int getItemCount() {
        return adminList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemRunnerListBinding itemAdminBinding;

        public ViewHolder(ItemRunnerListBinding itemAdminBinding) {
            super(itemAdminBinding.getRoot());
            this.itemAdminBinding = itemAdminBinding;
        }
    }
}
