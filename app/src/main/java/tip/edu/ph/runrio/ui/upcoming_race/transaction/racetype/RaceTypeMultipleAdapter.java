package tip.edu.ph.runrio.ui.upcoming_race.transaction.racetype;

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
import tip.edu.ph.runrio.databinding.ItemTransactionRaceTypeMultipleBinding;
import tip.edu.ph.runrio.databinding.ItemTransactionRaceTypeSingleBinding;
import tip.edu.ph.runrio.model.data.RaceType;


public class RaceTypeMultipleAdapter extends RecyclerView.Adapter<RaceTypeMultipleAdapter.ViewHolder> {
    private List<RaceType> event;
    private final Context context;
    private final RaceTypeTransactionView view;
    private static final int VIEW_TYPE_DEFAULT = 0;
    int runnerMany1K;
     List<Integer> counterValue = new ArrayList<Integer>();



    public RaceTypeMultipleAdapter(Context context, RaceTypeTransactionView view) {
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
        ItemTransactionRaceTypeMultipleBinding itemEventBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_transaction_race_type_multiple,
                parent,
                false);
        return new ViewHolder(itemEventBinding);
    }

    @Override
    public void onBindViewHolder(final RaceTypeMultipleAdapter.ViewHolder holder, final int position) {
        holder.itemEventBinding.setRace(event.get(position));
        holder.itemEventBinding.setView(view);



        counterValue.add(position,Integer.parseInt(holder.itemEventBinding.transactionRaceTypeMultipleText.getText().toString()));

        holder.itemEventBinding.transactionRaceTypeMultipleAdd.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                runnerMany1K = Integer.parseInt(holder.itemEventBinding.transactionRaceTypeMultipleText.getText().toString());
                runnerMany1K += 1;
                holder.itemEventBinding.transactionRaceTypeMultipleText.setText(runnerMany1K+"");
                counterValue.set(position,runnerMany1K);
            }
        });

        holder.itemEventBinding.transactionRaceTypeMultipleMinus.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                runnerMany1K = Integer.parseInt(holder.itemEventBinding.transactionRaceTypeMultipleText.getText().toString());
                if(!(runnerMany1K == 0)) {
                    runnerMany1K -= 1;
                    holder.itemEventBinding.transactionRaceTypeMultipleText.setText(runnerMany1K+"");
                    counterValue.set(position,runnerMany1K);
                }
            }
        });



    }


    public void clear() {
        event.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return event.size();
    }


    public List<Integer> getListValue()
    {
        return counterValue;
    }

    public void setUpcomingRaces(List<RaceType> event) {
        this.event = event;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTransactionRaceTypeMultipleBinding itemEventBinding;

        public ViewHolder(final ItemTransactionRaceTypeMultipleBinding itemEventBinding) {
            super(itemEventBinding.getRoot());
            this.itemEventBinding = itemEventBinding;



        }
    }
}
