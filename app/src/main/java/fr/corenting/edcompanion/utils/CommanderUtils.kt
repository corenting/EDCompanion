package fr.corenting.edcompanion.utils

import android.content.Context
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.network.player.EDSMPlayer
import fr.corenting.edcompanion.network.player.FrontierPlayer
import fr.corenting.edcompanion.network.player.InaraPlayer

object CommanderUtils {
    fun getCommanderName(context: Context): String? {
        val edsmPlayer = EDSMPlayer(context)
        val inaraPlayer = InaraPlayer(context)

        if (edsmPlayer.isUsable()) {
            return SettingsUtils.getString(
                context,
                context.getString(R.string.settings_cmdr_edsm_username)
            )
        } else if (inaraPlayer.isUsable()) {
            return inaraPlayer.getCommanderName()
        }

        return null
    }

    fun hasFleetData(context: Context): Boolean {
        val frontierPlayer = FrontierPlayer(context)

        if (frontierPlayer.isUsable()) {
            return  true
        }
        return false
    }

    fun hasCreditsData(context: Context): Boolean {
        val edsmPlayer = EDSMPlayer(context)

        if (edsmPlayer.isUsable()) {
            return true
        }

        return false
    }

    fun hasPositionData(context: Context): Boolean {
        val edsmPlayer = EDSMPlayer(context)

        if (edsmPlayer.isUsable()) {
            return true
        }

        return false
    }

    fun hasCommanderInformations(context: Context): Boolean {
        val edsmPlayer = EDSMPlayer(context)
        val inaraPlayer = InaraPlayer(context)

        return edsmPlayer.isUsable() || inaraPlayer.isUsable()
    }

    fun setCachedCurrentCommanderPosition(context: Context, systemName: String) {
        SettingsUtils.setString(
            context,
            context.getString(R.string.commander_position_cache_key),
            systemName
        )
    }

    fun getCachedCurrentCommanderPosition(context: Context): String? {
        return SettingsUtils.getString(
            context,
            context.getString(R.string.commander_position_cache_key)
        )
    }
}