package fr.corenting.edcompanion.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentPagerAdapter
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.databinding.ActivityFragmentsWithTabsBinding
import fr.corenting.edcompanion.utils.ThemeUtils

abstract class AbstractViewPagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFragmentsWithTabsBinding
    protected lateinit var dataName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeUtils.setTheme(this)
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentsWithTabsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Get data name from intent
        dataName = getDefaultData()
        dataName = intent.extras?.getString("data", getDefaultData()).toString()

        // Set toolbar
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        // Viewpager
        binding.viewPager.offscreenPageLimit = 4
        binding.viewPager.adapter = getPagerAdapter()

        // TabLayout
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        if (ThemeUtils.isDarkThemeEnabled(this)) {
            binding.tabLayout.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.primaryColorDark
                )
            )
        } else {
            binding.tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.primaryColor))
        }
        binding.tabLayout.setTabTextColors(
            ContextCompat.getColor(this, R.color.tabTextSelected),
            ContextCompat.getColor(this, R.color.tabText)
        )

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