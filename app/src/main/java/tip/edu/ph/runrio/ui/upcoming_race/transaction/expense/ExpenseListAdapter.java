package tip.edu.ph.runrio.ui.upcoming_race.transaction.expense;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.databinding.ItemExpenseBinding;
import tip.edu.ph.runrio.databinding.ItemRunnerTransactionListBinding;
import tip.edu.ph.runrio.model.data.Profile;
import tip.edu.ph.runrio.model.data.RealmString;
import tip.edu.ph.runrio.model.data.RealmString2;
import tip.edu.ph.runrio.model.data.TransactionData;
import tip.edu.ph.runrio.ui.upcoming_race.transaction.profiletab.profilefragment.ProfileView;


public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ViewHolder> {

    private final Context context;
    private final ExpenseView view;
    private List<String> raceType;
    private List<String> runner;
    private List<String> price;

    private int controlNumber = 0;
    private int runnerCtr = 0;
    private Realm realm;

    public ExpenseListAdapter(Context context, ExpenseView view) {
        this.context = context;
        this.view = view;
        raceType = new ArrayList<>();
        runner = new ArrayList<>();
        price = new ArrayList<>();
    }

    public void setBreakList(List<String> raceType, List<String> runner, List<String> price) {
        this.raceType = raceType;
        this.runner = runner;
        this.price = price;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemExpenseBinding itemAdminBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_expense,
                parent,
                false);
        return new ViewHolder(itemAdminBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        // holder.itemAdminBinding.setRunner(adminList.get(position));
        //holder.itemAdminBinding.setView(view);

        holder.itemAdminBinding.breakdownraceprice.setText(price.get(position));
        holder.itemAdminBinding.breakdownracetype.setText(raceType.get(position));
        holder.itemAdminBinding.breakdownrunner.setText(runner.get(position));


    }

    @Override
    public int getItemCount() {
        return raceType.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemExpenseBinding itemAdminBinding;

        public ViewHolder(ItemExpenseBinding itemAdminBinding) {
            super(itemAdminBinding.getRoot());
            this.itemAdminBinding = itemAdminBinding;

        }

    }
   }


