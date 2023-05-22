package fr.corenting.edcompanion.activities

import androidx.fragment.app.FragmentPagerAdapter
import fr.corenting.edcompanion.adapters.SystemDetailsPagerAdapter
import fr.corenting.edcompanion.network.SystemNetwork

class SystemDetailsActivity : AbstractViewPagerActivity() {

    override fun getDefaultData(): String {
        return "Sol"
    }

    override fun getPagerAdapter(): FragmentPagerAdapter {
        return SystemDetailsPagerAdapter(supportFragmentManager, this)
    }

    override fun getData() {
        SystemNetwork.getSystemDetails(this, dataName)
        SystemNetwork.getSystemHistory(this, dataName)
        SystemNetwork.getSystemStations(this, dataName)
    }
}
