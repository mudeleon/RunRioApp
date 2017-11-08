package tip.edu.ph.runrio.ui.upcoming_race.transaction.profiletab.profilefragment;

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.databinding.ItemRunnerTransactionListBinding;
import tip.edu.ph.runrio.model.data.Profile;
import tip.edu.ph.runrio.model.data.RealmString;
import tip.edu.ph.runrio.model.data.RealmString2;
import tip.edu.ph.runrio.model.data.TransactionData;


public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.ViewHolder> {

    private final Context context;
    private final ProfileView view;
    private List<Profile> adminList;
    private List<String> raceKit;
    private List<String> raceChoices;
    private RealmList<RealmString> raceKitData;
    private RealmList<RealmString2> raceChoicesData;
    private int controlNumber=0;
    private int runnerCtr=0;
    private Realm realm;

    public ProfileListAdapter(Context context, ProfileView view) {
        this.context = context;
        this.view = view;
        adminList = new ArrayList<>();
        raceChoices = new ArrayList<>();
        raceKit = new ArrayList<>();
        raceKitData = new RealmList<RealmString>();
        raceChoicesData = new RealmList<RealmString2>();


    }

    public void setRunnerList(List<Profile> adminList,List<String> raceKit,List<String> raceChoices, int controlNumber) {
        this.adminList = adminList;
        this.raceKit = raceKit;
        this.raceChoices = raceChoices;
        this.controlNumber = controlNumber;
        this.runnerCtr = (controlNumber+1);
        this.realm = Realm.getDefaultInstance();
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

        holder.itemAdminBinding.runnerListNumber.setText("Runner # "+ (runnerCtr+position));
        if(!(adminList.get(position).getNullCheckerTransactionList()!=null))
        {
           // holder.itemAdminBinding.runnerListNumber.setText("Runner # "+ (controlNumber+1));
            holder.itemAdminBinding.addRunner.setVisibility(View.GONE);
            holder.itemAdminBinding.layout.setVisibility(View.VISIBLE);
            holder.itemAdminBinding.replaceRunner.setVisibility(View.VISIBLE);
            holder.itemAdminBinding.runnerTransactionListGender.setVisibility(View.VISIBLE);
            holder.itemAdminBinding.runnerTransactionListName.setVisibility(View.VISIBLE);
            holder.itemAdminBinding.choicestitle.setVisibility(View.VISIBLE);

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


            raceKitData.clear();
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
           for( int a=0;a<raceKit.size();a++) {

               RadioGroup radioGroup2 = new RadioGroup(context);
               radioGroup2.setOrientation(LinearLayout.HORIZONTAL);


               TextView valueTV = new TextView(context);
               valueTV.setText(raceKit.get(a));
               valueTV.setOnClickListener(mThisButtonListener);
               itemAdminBinding.layout.addView(valueTV,p);


               String[] items = (raceChoices.get(a)).split("%");

               for (String item : items) {

                   RadioButton radioButtonView = new RadioButton(context);
                   radioButtonView.setText(item);
                   radioButtonView.setOnClickListener(mThisButtonListener);
                   radioButtonView.setTag(controlNumber+":"+a);
                   radioGroup2.addView(radioButtonView, p);

               }

                final String  tempKit = raceKit.get(a);
               final int ctr = a;
               realm.executeTransaction(new Realm.Transaction() {
                   @Override
                   public void execute(Realm realm) {


                       RealmString string =  new RealmString();
                       string.setId(ctr);
                       string.setVal(tempKit);
                       string = realm.copyToRealmOrUpdate(string);
                       raceKitData.add(string);

                   }
               });

               itemAdminBinding.layout.addView(radioGroup2,p2);

           }
            TransactionData data = realm.where(TransactionData.class)
                    .equalTo("id", controlNumber)
                    .findFirst();

                realm.beginTransaction();
                data.setTransactionDataRaceKit(raceKitData);
                realm.commitTransaction();
            controlNumber ++;
        }

    }
    private View.OnClickListener mThisButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            try {

                raceChoicesData.clear();
                String[] separated = (v.getTag().toString()).split(":");

                final String choice = ((RadioButton) v).getText().toString();
                final int choiceid = Integer.parseInt(separated[1]);
              /*  Toast.makeText(context, choice+separated[0]+" : " + separated[1],
                        Toast.LENGTH_SHORT).show();
                */


                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmString2 string =  new RealmString2();
                        string.setId(choiceid);
                        string.setVal(choice);
                        string = realm.copyToRealmOrUpdate(string);
                        raceChoicesData.add(string);

                    }
                });



                final TransactionData data = realm.where(TransactionData.class)
                        .equalTo("id", Integer.parseInt(separated[0]))
                        .findFirst();
                Log.d(" 1>>>",data+"");
                realm.beginTransaction();
                data.setTransactionDataRaceKitChoices(raceChoicesData);
                Log.d(" 2>>>",data.getTransactionDataRaceKitChoices().get(0).getVal()+"");
                realm.commitTransaction();


            }
            catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    };

}
