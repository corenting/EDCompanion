package fr.corenting.edcompanion.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import fr.corenting.edcompanion.fragments.SystemDetailsFragment
import fr.corenting.edcompanion.fragments.SystemFactionsFragment
import fr.corenting.edcompanion.fragments.SystemStationsFragment

class SystemDetailsPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> SystemStationsFragment()
            2 -> SystemFactionsFragment()
            else -> SystemDetailsFragment()
        }
    }
}
