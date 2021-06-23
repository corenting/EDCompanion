package fr.corenting.edcompanion.activities

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.text.HtmlCompat
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.databinding.ActivityAboutBinding
import fr.corenting.edcompanion.utils.ThemeUtils

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(ThemeUtils.getThemeToUse(this))
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Action bar setup
        setSupportActionBar(binding.includeAppBar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var version: String? = " version "
        try {
            val info: PackageInfo = packageManager.getPackageInfo(packageName, 0)
            version += info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            binding.versionTextView.visibility = View.GONE
        }
        binding.versionTextView.text = version
        setHtmlTextView(binding.librariesContentTextView, getString(R.string.about_libs))
        setHtmlTextView(binding.iconsContentTextView, getString(R.string.about_icons))
        setHtmlTextView(binding.contactTextView, getString(R.string.about_contact_me))
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