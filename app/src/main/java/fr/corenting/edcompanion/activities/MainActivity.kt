package fr.corenting.edcompanion.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.shape.MaterialShapeDrawable
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.databinding.ActivityMainBinding
import fr.corenting.edcompanion.fragments.*
import fr.corenting.edcompanion.utils.*
import fr.corenting.edcompanion.view_models.CommanderViewModel
import fr.corenting.edcompanion.view_models.ServerStatusViewModel
import java.security.AccessController.getContext


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        private const val KEY_CURRENT_TITLE = "CURRENT_TITLE"
        private const val KEY_CURRENT_SUBTITLE = "CURRENT_SUBTITLE"
        private const val KEY_CURRENT_TAG = "CURRENT_TAG"
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var fragmentManager: FragmentManager
    private lateinit var currentTitle: CharSequence
    private lateinit var currentSubtitle: CharSequence
    private lateinit var currentFragmentTag: String

    private lateinit var serverStatusViewModel: ServerStatusViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(ThemeUtils.getThemeToUse(this))
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Set toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Drawer toggle
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        binding.drawerLayout.addDrawerListener(HideKeyboardDrawerListener(binding.drawerLayout.rootView))
        toggle.syncState()

        // Setup navigation view and fake click the first item
        binding.navView.setNavigationItemSelectedListener(this)

        // Set initial fragment
        fragmentManager = supportFragmentManager
        if (savedInstanceState == null) {
            binding.navView.setCheckedItem(binding.navView.menu.getItem(0).itemId)
            switchOnNavigation(getString(R.string.galnet), R.id.nav_galnet_news)
        } else {
            currentTitle = savedInstanceState.getCharSequence(KEY_CURRENT_TITLE).toString()
            currentSubtitle = savedInstanceState.getCharSequence(KEY_CURRENT_SUBTITLE).toString()
            currentFragmentTag = savedInstanceState.getString(KEY_CURRENT_TAG).toString()
            val fragment = fragmentManager.findFragmentByTag(currentFragmentTag)
            ViewUtils.switchFragment(fragmentManager, fragment, currentFragmentTag)
            updateActionBar()
        }

        // Set listener on server status text to refresh it, and set theme
        val drawerSubtitleTextView = binding.navView.getHeaderView(0)
            .findViewById<TextView>(R.id.drawerSubtitleTextView)
        drawerSubtitleTextView.setOnClickListener { updateServerStatus() }

        // Push notifications setup
        NotificationsUtils.refreshPushSubscriptions(this)

        // Show changelog
        ChangelogUtils.showChangelog(this)

        // Server status
        val serverStatusModel: ServerStatusViewModel by viewModels()
        serverStatusViewModel = serverStatusModel
        serverStatusViewModel.getServerStatus().observe(this) { status ->
            val content: String =
                if (status?.error == null && status.data != null) status.data.status else getString(
                    R.string.unknown
                )
            drawerSubtitleTextView.text = getString(R.string.server_status, content)
        }
        serverStatusViewModel.fetchServerStatus()

        // Get commander position in background to update cached value
        val commanderModel: CommanderViewModel by viewModels()
        commanderModel.fetchPosition()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putCharSequence(KEY_CURRENT_TITLE, currentTitle)
        outState.putCharSequence(KEY_CURRENT_SUBTITLE, currentSubtitle)
        outState.putString(KEY_CURRENT_TAG, currentFragmentTag)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return switchOnNavigation(item.title.toString(), item.itemId)
    }

    override fun onResume() {
        updateActionBar()
        super.onResume()
    }

    private fun updateActionBar() {
        supportActionBar?.subtitle = currentSubtitle
        title = currentTitle
    }

    private fun updateServerStatus() {
        val textView = binding.navView.getHeaderView(0)
            .findViewById<TextView>(R.id.drawerSubtitleTextView)
        textView.text = getString(R.string.updating_server_status)
        serverStatusViewModel.fetchServerStatus()
    }

    private fun switchOnNavigation(title: String, id: Int): Boolean {
        // Set title and subtitle default values
        if (id != R.id.nav_settings && id != R.id.nav_about) {
            currentTitle = title
            currentSubtitle = ""
        }

        when (id) {
            R.id.nav_cg -> {
                switchFragment(CommunityGoalsFragment.COMMUNITY_GOALS_FRAGMENT_TAG)
                currentSubtitle = getString(R.string.inara_credits)
            }
            R.id.nav_cmdr -> {
                switchFragment(CommanderFragment.COMMANDER_FRAGMENT)
                val commanderName = CommanderUtils.getCommanderName(this)
                currentTitle = if (commanderName.isEmpty())
                    getString(R.string.commander)
                else
                    commanderName
            }
            R.id.nav_galnet_news -> {
                switchFragment(GalnetFragment.GALNET_FRAGMENT_TAG)
            }
            R.id.nav_news -> {
                switchFragment(NewsFragment.NEWS_FRAGMENT_TAG)
            }
            R.id.nav_systems -> {
                switchFragment(SystemFinderFragment.SYSTEM_FINDER_FRAGMENT_TAG)
                currentSubtitle = getString(R.string.edsm_credits)
            }
            R.id.nav_distance_calculator -> {
                switchFragment(DistanceCalculatorFragment.DISTANCE_CALCULATOR_FRAGMENT_TAG)
                currentSubtitle = getString(R.string.eddb_credits)
            }
            R.id.nav_commodity_finder -> {
                switchFragment(CommodityFinderFragment.COMMODITY_FINDER_FRAGMENT_TAG)
                currentSubtitle = getString(R.string.eddb_eddn_credits)
            }
            R.id.nav_commodities_list -> {
                switchFragment(CommoditiesListFragment.COMMODITIES_LIST_FRAGMENT_TAG)
                currentSubtitle = getString(R.string.eddb_eddn_credits)
            }
            R.id.nav_ship_finder -> {
                switchFragment(ShipFinderFragment.SHIP_FINDER_FRAGMENT_TAG)
                currentSubtitle = getString(R.string.eddb_credits)
            }
            R.id.nav_about -> {
                val i = Intent(this, AboutActivity::class.java)
                startActivity(i)
                return false
            }
            R.id.nav_settings -> {
                val i = Intent(this, SettingsActivity::class.java)
                startActivity(i)
                return false
            }
        }

        updateActionBar()
        return true
    }

    private fun switchFragment(tag: String) {
        currentFragmentTag = tag

        // Get previous fragment if exists
        val fragment = fragmentManager.findFragmentByTag(tag)
        if (fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.fragmentContent, fragment, tag).commit()
            return
        }

        // Else
        when (tag) {
            GalnetFragment.GALNET_FRAGMENT_TAG -> ViewUtils.switchFragment(
                fragmentManager,
                GalnetFragment(),
                tag
            )
            NewsFragment.NEWS_FRAGMENT_TAG -> ViewUtils.switchFragment(
                fragmentManager,
                NewsFragment(),
                tag
            )
            SystemFinderFragment.SYSTEM_FINDER_FRAGMENT_TAG -> ViewUtils.switchFragment(
                fragmentManager,
                SystemFinderFragment(),
                tag
            )
            CommanderFragment.COMMANDER_FRAGMENT -> ViewUtils.switchFragment(
                fragmentManager,
                CommanderFragment(),
                tag
            )
            DistanceCalculatorFragment.DISTANCE_CALCULATOR_FRAGMENT_TAG -> ViewUtils.switchFragment(
                fragmentManager,
                DistanceCalculatorFragment(),
                tag
            )
            ShipFinderFragment.SHIP_FINDER_FRAGMENT_TAG -> ViewUtils.switchFragment(
                fragmentManager,
                ShipFinderFragment(),
                tag
            )
            CommodityFinderFragment.COMMODITY_FINDER_FRAGMENT_TAG -> ViewUtils.switchFragment(
                fragmentManager,
                CommodityFinderFragment(),
                tag
            )
            CommoditiesListFragment.COMMODITIES_LIST_FRAGMENT_TAG -> ViewUtils.switchFragment(
                fragmentManager,
                CommoditiesListFragment(),
                tag
            )
            else -> ViewUtils.switchFragment(fragmentManager, CommunityGoalsFragment(), tag)
        }
    }
}