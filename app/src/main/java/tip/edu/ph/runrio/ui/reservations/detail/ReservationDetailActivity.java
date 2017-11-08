package tip.edu.ph.runrio.ui.reservations.detail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import io.realm.Realm;
import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.app.Constants;
import tip.edu.ph.runrio.app.Endpoints;
import tip.edu.ph.runrio.databinding.ActivityReservationDetailBinding;
import tip.edu.ph.runrio.model.data.Reservation;
import tip.edu.ph.runrio.model.data.User;
import tip.edu.ph.runrio.ui.reservations.runners.RunnerListActivity;


public class ReservationDetailActivity extends MvpActivity<ReservationDetailView, ReservationDetailPresenter> implements ReservationDetailView {

    ActivityReservationDetailBinding binding;
    private int id;
    private Reservation reservation;
    private RunnerListAdapter adapter;
    private Realm realm;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reservation_detail);
        binding.setView(getMvpView());
        presenter.onStart();

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id = getIntent().getIntExtra(Constants.ID, -1);
        reservation = presenter.reservation(id);
        binding.setRes(reservation);
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        Log.d("TAG", user+"");
        String imageURL = "https://payapp.tip.edu.ph/api/storage/app/image/default_buyer.png";
        if (user.isLoaded() && user.isValid()) {
            if (user.getImage() != null && !user.getImage().isEmpty()) {
                imageURL = Endpoints.IMAGE_URL.replace(Endpoints.IMG_HOLDER, user.getImage());
                Log.d("TAG", imageURL);
            }
            binding.setUser(user);
        }
        Glide.with(this).load(imageURL).centerCrop().into(binding.reservedByImage);


        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RunnerListAdapter(getMvpView());
        binding.recyclerView.setAdapter(adapter);
        if (reservation.getReservationRunnerReservationCategory().size() > 3) {
            adapter.setData(reservation.getReservationRunnerReservationCategory().subList(0, 3));
        } else {

            adapter.setData(reservation.getReservationRunnerReservationCategory());
        }

        reservation = presenter.reservation(id);

        if (reservation.getReservationClaimingReservationCategory().getClaimingReservationType().equals("P")) {
            binding.claimingType.setText("PICKUP");
            binding.claimingImageView.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_building).colorRes(R.color.white));
        } else if (reservation.getReservationClaimingReservationCategory().getClaimingReservationType().equals("D")) {
            binding.claimingType.setText("DELIVERY");
            binding.claimingImageView.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_truck).colorRes(R.color.white));
        }

        if (reservation.getReservationPaymentStatus().equals("P")) {
            binding.paymentStatus.setText("PAID");
            binding.paymentImageView.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_money).colorRes(R.color.white));
        } else {
            binding.paymentStatus.setText("PENDING");
            binding.paymentImageView.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_clock_o).colorRes(R.color.white));
        }


        if(reservation.getReservationClaimingReservationCategory().getClaimingReservationStatus().equals("F")){
            binding.isClaimed.setBackgroundColor(ContextCompat.getColor(this,R.color.lightGray));
        }else if(reservation.getReservationClaimingReservationCategory().getClaimingReservationStatus().equals("C")){
            binding.isClaimed.setBackgroundColor(ContextCompat.getColor(this,R.color.greenSuccess));
        }

    }

    @Override
    public void onViewAllRunners(Reservation res) {
        Intent intent = new Intent(this, RunnerListActivity.class);
        intent.putExtra(Constants.ID, reservation.getId());
        startActivity(intent);
    }

    @NonNull
    @Override
    public ReservationDetailPresenter createPresenter() {
        return new ReservationDetailPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
        realm.close();
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

}
