package fr.corenting.edcompanion.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.events.FrontierTokensEvent;
import fr.corenting.edcompanion.singletons.FrontierAuthSingleton;
import fr.corenting.edcompanion.utils.ThemeUtils;


public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.login_dialog_title)
                    .setMessage(R.string.login_dialog_text)
                    .setPositiveButton(android.R.string.ok, (dialog1, which) -> {
                        dialog1.dismiss();
                        launchAuthCodeStep();
                    })
                    .setNegativeButton(android.R.string.cancel,
                            (dialog2, which) -> {
                                dialog2.dismiss();
                                finish();
                            })
                    .create();

            dialog.show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginTokensEvent(FrontierTokensEvent tokens) {
        if (tokens.getSuccess()) {
            // TODO : OK
        } else {
            // TODO : failure
        }
    }

    private void launchAuthCodeStep() {
        // Authorization step
        String url = FrontierAuthSingleton.getInstance()
                .getAuthorizationUrl(getApplicationContext());

        launchBrowserIntent(url);
    }

    private void launchTokensStep(String authCode, String state) {
        // Tokens exchange step
        FrontierAuthSingleton.getInstance()
                .sendTokensRequest(getApplicationContext(), authCode, state);

    }

    private void launchBrowserIntent(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setFlags(browserIntent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        browserIntent.setData(Uri.parse(url));
        startActivity(browserIntent);
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
}
