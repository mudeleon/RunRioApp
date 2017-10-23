package tip.edu.ph.runrio.ui.runner.detail;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import tip.edu.ph.runrio.R;
import tip.edu.ph.runrio.app.Constants;
import tip.edu.ph.runrio.databinding.ActivityAddRunnerBinding;
import tip.edu.ph.runrio.model.data.User;


public class RunnerDetailActivity extends MvpViewStateActivity<RunnerDetailView, RunnerDetailPresenter> implements RunnerDetailView {

    private static final String TAG = RunnerDetailActivity.class.getSimpleName();
    private ActivityAddRunnerBinding binding;
    private Realm realm;
    private User user;

    private ProgressDialog progressDialog;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_runner);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");
        progressDialog.setCancelable(false);

        final int id = getIntent().getIntExtra(Constants.ID, -1);
        if (id == -1) {
            Log.d(TAG, "no int extra found");
            Toast.makeText(this, "Error Passing Promo ID", Toast.LENGTH_SHORT).show();
            finish();
        }

    }



    @Override
    protected void onDestroy() {


        realm.close();
        super.onDestroy();
    }

    @NonNull
    @Override
    public RunnerDetailPresenter createPresenter() {
        return new RunnerDetailPresenter();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      //  getMenuInflater().inflate(R.menu.menu_chat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_message:
                Toast.makeText(this, "Coming Soon..", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.show();
    }

    @Override
    public void showAlert(String message) {
        new AlertDialog.Builder(this)
                .setTitle(message)
                .setPositiveButton("Close", null)
                .show();
    }

    @NonNull
    @Override
    public ViewState<RunnerDetailView> createViewState() {
        setRetainInstance(true);
        return new RunnerDetailViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }
}
