package fr.corenting.edcompanion.activities

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.databinding.ActivityFragmentsWithTabsBinding
import fr.corenting.edcompanion.utils.ThemeUtils

abstract class AbstractViewPagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFragmentsWithTabsBinding
    protected lateinit var dataName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(ThemeUtils.getThemeToUse(this))
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

        // Setup tablayout and viewpager
        binding.viewPager.adapter = getAdapter(this)
        binding.viewPager.offscreenPageLimit = 4
        getTabLayoutMediator(this, binding.tabLayout, binding.viewPager).attach()
        binding.viewPager.children.find { it is RecyclerView }?.let {
            (it as RecyclerView).isNestedScrollingEnabled = false
        }
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

    abstract fun getAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter

    abstract fun getTabLayoutMediator(context: Context, tabLayout: TabLayout, viewPager: ViewPager2): TabLayoutMediator
    abstract fun getData()
}