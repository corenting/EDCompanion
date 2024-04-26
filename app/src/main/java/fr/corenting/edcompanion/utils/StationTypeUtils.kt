package fr.corenting.edcompanion.utils

import android.content.Context
import fr.corenting.edcompanion.R

object StationTypeUtils {
    fun getStationTypeText(context: Context, isPlanetary: Boolean, isFleetCarrier: Boolean, isSettlement: Boolean): String? {
        return when {
            isPlanetary -> {
                context.getString(R.string.planetary)
            }

            isFleetCarrier -> {
                context.getString(R.string.fleet_carrier)
            }

            isSettlement -> {
                context.getString(R.string.settlement)
            }

            else -> null
        }
    }
}