package tip.edu.ph.runrio.ui.reservations;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import io.realm.Realm;
import io.realm.RealmResults;
import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.app.Constants;
import tip.edu.ph.runrio.databinding.ActivityReservationsBinding;
import tip.edu.ph.runrio.model.data.Reservation;
import tip.edu.ph.runrio.ui.reservations.detail.ReservationDetailActivity;


public class ReservationsActivity extends MvpActivity<ReservationsView, ReservationsPresenter> implements ReservationsView {

    ActivityReservationsBinding binding;
    private int id;
    private RealmResults<Reservation> raceEvent;
    private ReservationListAdapter adapter;
    private Realm realm;
    private String TAG = ReservationsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reservations);
        binding.setView(getMvpView());
        presenter.onStart();

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        id = getIntent().getIntExtra(Constants.ID, -1);
       // raceEvent = presenter.raceEvent(id);
        realm = Realm.getDefaultInstance();
        raceEvent = realm.where(Reservation.class).equalTo(Constants.ID, id).findAll();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReservationListAdapter(getMvpView());
        binding.recyclerView.setAdapter(adapter);
        adapter.setData(raceEvent);

        if(raceEvent.size()>0){

            binding.includeResult.resultLayout.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
        }else {
            binding.includeResult.resultLayout.setVisibility(View.VISIBLE);
            binding.includeResult.resultTv.setText("No Reservations Yet");
            binding.recyclerView.setVisibility(View.GONE);
        }


    }

    @Override
    public void onItemClicked(Reservation reservation) {
        Intent intent = new Intent(this, ReservationDetailActivity.class);
        intent.putExtra(Constants.ID, reservation.getId());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
    }

    @NonNull
    @Override
    public ReservationsPresenter createPresenter() {
        return new ReservationsPresenter();
    }
}
