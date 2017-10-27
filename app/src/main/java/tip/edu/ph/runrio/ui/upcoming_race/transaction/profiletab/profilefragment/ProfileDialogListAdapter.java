package tip.edu.ph.runrio.ui.upcoming_race.transaction.profiletab.profilefragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.databinding.ItemRunnerListBinding;
import tip.edu.ph.runrio.databinding.ItemRunnerListDialogBinding;
import tip.edu.ph.runrio.model.data.Profile;
import tip.edu.ph.runrio.ui.runner.list.RunnerListView;


public class ProfileDialogListAdapter extends RecyclerView.Adapter<ProfileDialogListAdapter.ViewHolder> {

    private final Context context;
    private final ProfileView view;
    private List<Profile> adminList;

    public ProfileDialogListAdapter(Context context, ProfileView view) {
        this.context = context;
        this.view = view;
        adminList = new ArrayList<>();
    }

    public void setRunnerList(List<Profile> adminList) {
        this.adminList = adminList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemRunnerListDialogBinding itemAdminBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_runner_list_dialog,
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
        private final ItemRunnerListDialogBinding itemAdminBinding;

        public ViewHolder(ItemRunnerListDialogBinding itemAdminBinding) {
            super(itemAdminBinding.getRoot());
            this.itemAdminBinding = itemAdminBinding;
        }
    }
}
