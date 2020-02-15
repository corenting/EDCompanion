package fr.corenting.edcompanion.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.events.FrontierTokensEvent;
import fr.corenting.edcompanion.singletons.FrontierAuthSingleton;
import fr.corenting.edcompanion.utils.ThemeUtils;


public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.progressTextView)
    public TextView progressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //Action bar setup
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Check step (back from browser or just opened)
        if (getIntent() != null && getIntent().getAction() != null &&
                getIntent().getAction().equals(Intent.ACTION_VIEW)) {
            Uri uri = getIntent().getData();

            if (uri != null) {
                String code = uri.getQueryParameter("code");
                String state = uri.getQueryParameter("state");
                launchTokensStep(code, state);
            }
        } else {
            // Show dialog
            AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.login_dialog_title)
                    .setMessage(R.string.login_dialog_text)
                    .setPositiveButton(android.R.string.ok,
                            (d, which) -> {
                                d.dismiss();
                                launchAuthCodeStep();
                            })
                    .setNegativeButton(android.R.string.cancel,
                            (d, which) -> {
                                d.dismiss();
                                finish();
                            })
                    .create();

            dialog.show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginTokensEvent(FrontierTokensEvent tokens) {
        if (tokens.getSuccess()) {
            Toast t = Toast.makeText(this, R.string.account_linked, Toast.LENGTH_SHORT);
            t.show();
            setResult(Activity.RESULT_OK);
            finish();
        } else {
            AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.login_dialog_error_title)
                    .setMessage(R.string.login_dialog_error_text)
                    .setOnCancelListener(d -> finish())
                    .setOnDismissListener(d -> finish())
                    .setPositiveButton(android.R.string.ok, (d, which) -> d.dismiss())
                    .setNegativeButton(android.R.string.cancel,
                            (d, which) -> d.dismiss())
                    .create();

            dialog.show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void launchAuthCodeStep() {
        // Authorization step
        String url = FrontierAuthSingleton.getInstance()
                .getAuthorizationUrl(getApplicationContext());

        launchBrowserIntent(url);
        finish();
    }

    private void launchTokensStep(String authCode, String state) {
        // Tokens exchange step
        progressTextView.setText(R.string.adding_account);
        FrontierAuthSingleton.getInstance()
                .sendTokensRequest(getApplicationContext(), authCode, state);

    }

    private void launchBrowserIntent(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setFlags(browserIntent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        browserIntent.setData(Uri.parse(url));
        startActivity(browserIntent);
    }
}
