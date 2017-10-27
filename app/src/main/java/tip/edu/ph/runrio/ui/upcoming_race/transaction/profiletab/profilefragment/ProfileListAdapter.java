package tip.edu.ph.runrio.ui.upcoming_race.transaction.profiletab.profilefragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.databinding.ItemRunnerTransactionListBinding;
import tip.edu.ph.runrio.model.data.Profile;


public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.ViewHolder> {

    private final Context context;
    private final ProfileView view;
    private List<Profile> adminList;

    public ProfileListAdapter(Context context, ProfileView view) {
        this.context = context;
        this.view = view;
        adminList = new ArrayList<>();

    }

    public void setRunnerList(List<Profile> adminList) {
        this.adminList = adminList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemRunnerTransactionListBinding itemAdminBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_runner_transaction_list,
                parent,
                false);
        return new ViewHolder(itemAdminBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.itemAdminBinding.setRunner(adminList.get(position));
        holder.itemAdminBinding.setView(view);

        holder.itemAdminBinding.runnerListNumber.setText("Runner # "+(position+1));
        if(!(adminList.get(position).getNullCheckerTransactionList()!=null))
        {
            holder.itemAdminBinding.addRunner.setVisibility(View.GONE);
            holder.itemAdminBinding.replaceRunner.setVisibility(View.VISIBLE);
            holder.itemAdminBinding.runnerTransactionListGender.setVisibility(View.VISIBLE);
            holder.itemAdminBinding.runnerTransactionListName.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public int getItemCount() {
        return adminList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemRunnerTransactionListBinding itemAdminBinding;

        public ViewHolder(ItemRunnerTransactionListBinding itemAdminBinding) {
            super(itemAdminBinding.getRoot());
            this.itemAdminBinding = itemAdminBinding;
        }
    }
}
