package fr.corenting.edcompanion.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.fragments.CommanderFleetFragment
import fr.corenting.edcompanion.fragments.CommanderLoadoutsFragment
import fr.corenting.edcompanion.fragments.CommanderStatusFragment
import fr.corenting.edcompanion.utils.CommanderUtils.hasFleetData
import fr.corenting.edcompanion.utils.CommanderUtils.hasLoadoutData
import fr.corenting.edcompanion.utils.SettingsUtils

class CommanderFragmentPagerAdapter(val fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        val displayFleet = SettingsUtils.getBoolean(
            fragment.requireContext(),
            fragment.requireContext().getString(R.string.settings_cmdr_loadout_display_enable),
            true
        ) && hasLoadoutData(fragment.requireContext())
        return 1 + (if (hasFleetData(fragment.requireContext())) 1 else 0) + if (displayFleet) 1 else 0
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> CommanderFleetFragment()
            2 -> CommanderLoadoutsFragment()
            else -> CommanderStatusFragment()
        }
    }
}
