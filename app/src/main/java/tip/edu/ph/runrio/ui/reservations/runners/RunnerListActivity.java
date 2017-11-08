package tip.edu.ph.runrio.ui.reservations.runners;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import io.realm.RealmResults;
import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.app.Constants;
import tip.edu.ph.runrio.databinding.ActivityRunnerList2Binding;
import tip.edu.ph.runrio.model.data.Reservation;


public class RunnerListActivity extends MvpActivity<RunnerListView, RunnerListPresenter> implements RunnerListView {

    ActivityRunnerList2Binding binding;
    private int id;
    private Reservation reservation;
    private RunnerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_runner_list2);
        presenter.onStart();

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        id = getIntent().getIntExtra(Constants.ID, -1);
        reservation = presenter.reservation(id);
        binding.setRes(reservation);


        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RunnerListAdapter(getMvpView());
        binding.recyclerView.setAdapter(adapter);
        adapter.setData(reservation.getReservationRunnerReservationCategory());

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

    @NonNull
    @Override
    public RunnerListPresenter createPresenter() {
        return new RunnerListPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
    }
}
