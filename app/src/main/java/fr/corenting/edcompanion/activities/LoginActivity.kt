package fr.corenting.edcompanion.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.models.events.FrontierTokensEvent
import fr.corenting.edcompanion.singletons.FrontierAuthSingleton
import fr.corenting.edcompanion.utils.ThemeUtils
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeUtils.setTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Action bar setup
        val mToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(mToolbar)

        // Check step (back from browser or just opened)
        if (intent != null && intent.action != null &&
                intent.action == Intent.ACTION_VIEW) {
            val uri = intent.data

            val code = uri?.getQueryParameter("code")
            val state = uri?.getQueryParameter("state")

            if(code != null && state != null) {
                launchTokensStep(code, state)
            }
        } else {
            // Show dialog
            val dialog = MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.login_dialog_title)
                    .setMessage(R.string.login_dialog_text)
                    .setPositiveButton(android.R.string.ok
                    ) { d, _ ->
                        d.dismiss()
                        launchAuthCodeStep()
                    }
                    .setNegativeButton(android.R.string.cancel
                    ) { d, _ ->
                        d.dismiss()
                        finish()
                    }
                    .create()

            dialog.show()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginTokensEvent(tokens: FrontierTokensEvent) {
        if (tokens.success) {
            val t = Toast.makeText(this, R.string.account_linked, Toast.LENGTH_SHORT)
            t.show()
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            val dialog = MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.login_dialog_error_title)
                    .setMessage(R.string.login_dialog_error_text)
                    .setOnCancelListener { finish() }
                    .setOnDismissListener { finish() }
                    .setPositiveButton(android.R.string.ok) { d, _ -> d.dismiss() }
                    .setNegativeButton(android.R.string.cancel) { d, _ -> d.dismiss() }
                    .create()

            dialog.show()
        }
    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun launchAuthCodeStep() {
        // Authorization step
        val url = FrontierAuthSingleton.getInstance()
                .getAuthorizationUrl(applicationContext)

        launchBrowserIntent(url)
        finish()
    }

    private fun launchTokensStep(authCode: String, state: String) {
        // Tokens exchange step
        progressTextView.setText(R.string.adding_account)
        FrontierAuthSingleton.getInstance().sendTokensRequest(applicationContext, authCode, state)
    }

    private fun launchBrowserIntent(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW)
        browserIntent.flags = browserIntent.flags or Intent.FLAG_ACTIVITY_NO_HISTORY
        browserIntent.data = Uri.parse(url)
        startActivity(browserIntent)
    }
}
