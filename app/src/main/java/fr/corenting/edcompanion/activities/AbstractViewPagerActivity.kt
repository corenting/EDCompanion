package fr.corenting.edcompanion.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentPagerAdapter
import butterknife.ButterKnife
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.utils.ThemeUtils
import kotlinx.android.synthetic.main.activity_fragments_with_tabs.*

abstract class AbstractViewPagerActivity : AppCompatActivity() {

    protected lateinit var dataName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeUtils.setTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragments_with_tabs)
        ButterKnife.bind(this)

        // Get data name from intent
        dataName = getDefaultData()
        dataName = intent.extras?.getString("data", getDefaultData()).toString()

        // Set toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        // Viewpager
        viewPager.offscreenPageLimit = 4
        viewPager.adapter = getPagerAdapter()

        // TabLayout
        tabLayout.setupWithViewPager(viewPager)
        if (ThemeUtils.isDarkThemeEnabled(this)) {
            tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.primaryColorDark))
        } else {
            tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.primaryColor))
        }
        tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.tabTextSelected),
                ContextCompat.getColor(this, R.color.tabText))

        getData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    abstract fun getDefaultData(): String
    abstract fun getPagerAdapter(): FragmentPagerAdapter
    abstract fun getData()
}