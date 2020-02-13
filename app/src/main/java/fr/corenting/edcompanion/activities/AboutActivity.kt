package fr.corenting.edcompanion.activities

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.utils.ThemeUtils
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.include_app_bar.*

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeUtils.setTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        //Action bar setup
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var version: String? = " version "
        try {
            val info: PackageInfo = packageManager.getPackageInfo(packageName, 0)
            version += info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            versionTextView.visibility = View.GONE
        }
        versionTextView.text = version
        setHtmlTextView(librariesContentTextView, getString(R.string.about_libs))
        setHtmlTextView(iconsContentTextView, getString(R.string.about_icons))
        setHtmlTextView(contactTextView, getString(R.string.about_contact_me))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private fun setHtmlTextView(textView: TextView, text: String) {
            textView.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
            textView.movementMethod = LinkMovementMethod.getInstance()
        }
    }
}