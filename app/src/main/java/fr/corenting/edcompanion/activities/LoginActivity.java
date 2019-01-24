package fr.corenting.edcompanion.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

        // Check step
        if (getIntent() != null && getIntent().getAction().equals(Intent.ACTION_VIEW)) {
            Uri uri = getIntent().getData();

            if (uri != null) {
                String code = uri.getQueryParameter("code");
                String state = uri.getQueryParameter("state");
                launchTokensStep(code, state);
            }
        }
        else {
            launchAuthCodeStep();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTokensEvent(FrontierTokensEvent tokens) {
        if (tokens.getSuccess()) {
            // TODO : OK
        }
        else {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
