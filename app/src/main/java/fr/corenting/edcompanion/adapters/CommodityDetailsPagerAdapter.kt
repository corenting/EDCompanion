package fr.corenting.edcompanion.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import fr.corenting.edcompanion.fragments.CommodityDetailsBuyFragment
import fr.corenting.edcompanion.fragments.CommodityDetailsFragment
import fr.corenting.edcompanion.fragments.CommodityDetailsSellFragment


class CommodityDetailsPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> CommodityDetailsSellFragment()
            2 -> CommodityDetailsBuyFragment()
            else -> CommodityDetailsFragment()
        }
    }
}
