package tip.edu.ph.runrio.ui.upcoming_race.transaction.profiletab.profilefragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.app.Endpoints;
import tip.edu.ph.runrio.databinding.ActivityProfileBinding;
import tip.edu.ph.runrio.model.data.User;
import tip.edu.ph.runrio.util.CircleTransform;


public class ProfileInfoFragment extends MvpViewStateFragment<ProfileView, ProfilePresenter>
        implements ProfileView, SwipeRefreshLayout.OnRefreshListener{


    private ActivityProfileBinding binding;
    private Realm realm;

    public int dotsCount;
    private ImageView[] dots;
    User user;
    private ProgressDialog progressDialog;


    public ProfileInfoFragment() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @NonNull
    @Override
    public ProfilePresenter createPresenter() {
        return new ProfilePresenter();
    }

    @NonNull
    @Override
    public ViewState createViewState() {
        setRetainInstance(true);
        return new ProfileViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_profile, container, false);
        binding.swipeRefreshLayout.setOnRefreshListener(this);

        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        if (user.isLoaded() && user.isValid()) {
            binding.setUser(user);
            String imageURL = "https://payapp.tip.edu.ph/api/storage/app/image/default_buyer.png";
            if (user.getImage() != null && !user.getImage().isEmpty()) {
                imageURL = Endpoints.IMAGE_URL.replace(Endpoints.IMG_HOLDER, user.getImage());
                Log.d("TAG",imageURL);
            }
            if (user.getFbID() != null && !user.getFbID().isEmpty()) {
                imageURL = Endpoints.IMAGE_URL_FB.replace(Endpoints.IMG_HOLDER, user.getFbID());
            }
            Glide.with(getActivity())
                    .load(imageURL)
                    .transform(new CircleTransform(getActivity()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.layoutHeader.imageView);
        }
        //binding.setView(getMvpView());

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();


        user.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                if (user.isLoaded() && user.isValid()) {
                    binding.setUser(user);
                    String imageURL = "https://payapp.tip.edu.ph/api/storage/app/image/default_buyer.png";
                    if (user.getImage() != null && !user.getImage().isEmpty()) {
                        imageURL = Endpoints.IMAGE_URL.replace(Endpoints.IMG_HOLDER, user.getImage());
                        Log.d("TAG",imageURL);
                    }
                    if (user.getFbID() != null && !user.getFbID().isEmpty()) {
                        imageURL = Endpoints.IMAGE_URL_FB.replace(Endpoints.IMG_HOLDER, user.getFbID());
                    }
                    Glide.with(getActivity())
                            .load(imageURL)
                            .transform(new CircleTransform(getActivity()))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(binding.layoutHeader.imageView);
                }
            }
        });
        //binding.setView(getMvpView());

    }

    @Override
    public void onStop() {


        realm.close();
        super.onStop();
    }



    @Override
    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Connecting");
        }
        progressDialog.show();
    }

    @Override
    public void stopProgress() {
        if (progressDialog != null) progressDialog.dismiss();

    }
    @Override
    public void showAlert(String message) {
        if(!(message.contains("Successfully")))
            //   message = "Reservation Failed!";

            new AlertDialog.Builder(getActivity())
                    .setTitle(message)
                    .setPositiveButton( "Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();

    }


    @Override
    public void onChangePasswordClicked() {

    }

    @Override
    public void onRefresh() {
        // todo create refresh
        binding.swipeRefreshLayout.setRefreshing(false);
    }

}
