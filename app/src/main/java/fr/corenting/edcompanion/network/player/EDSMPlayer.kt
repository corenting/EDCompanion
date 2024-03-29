package fr.corenting.edcompanion.network.player

import android.content.Context
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.models.CommanderCredits
import fr.corenting.edcompanion.models.CommanderFleet
import fr.corenting.edcompanion.models.CommanderLoadout
import fr.corenting.edcompanion.models.CommanderLoadouts
import fr.corenting.edcompanion.models.CommanderPosition
import fr.corenting.edcompanion.models.CommanderRank
import fr.corenting.edcompanion.models.CommanderRanks
import fr.corenting.edcompanion.models.ProxyResult
import fr.corenting.edcompanion.network.retrofit.EDSMRetrofit
import fr.corenting.edcompanion.singletons.RetrofitSingleton
import fr.corenting.edcompanion.utils.SettingsUtils

class EDSMPlayer(val context: Context) : PlayerNetwork {

    private val edsmRetrofit: EDSMRetrofit = RetrofitSingleton.getInstance()
        .getEDSMRetrofit(context.applicationContext)
    private val apiKey =
        SettingsUtils.getString(context, context.getString(R.string.settings_cmdr_edsm_api_key))
    private val commanderName = SettingsUtils.getString(
        context,
        context.getString(R.string.settings_cmdr_edsm_username)
    )

    override fun getCommanderName(): String {
        return commanderName
    }

    override fun isUsable(): Boolean {
        val enabled =
            SettingsUtils.getBoolean(
                context,
                context.getString(R.string.settings_cmdr_edsm_enable),
                false
            )
        return enabled && !commanderName.isNullOrEmpty() && !apiKey.isNullOrEmpty()
    }

    override suspend fun getPosition(): ProxyResult<CommanderPosition> {
        return try {
            val apiPositions = edsmRetrofit.getPosition(apiKey, commanderName)

            ProxyResult(
                data = CommanderPosition(
                    systemName = apiPositions.system,
                    firstDiscover = apiPositions.firstDiscover
                ), error = null
            )
        } catch (t: Throwable) {
            ProxyResult(data = null, error = t)
        }
    }

    override suspend fun getRanks(): ProxyResult<CommanderRanks> {
        return try {
            val apiRanks = edsmRetrofit.getRanks(apiKey, commanderName)

            val combatRank = CommanderRank(
                apiRanks.ranksNames.combat,
                apiRanks.ranks.combat,
                apiRanks.progress.combat
            )
            val tradeRank = CommanderRank(
                apiRanks.ranksNames.trade,
                apiRanks.ranks.trade,
                apiRanks.progress.trade
            )
            val exploreRank = CommanderRank(
                apiRanks.ranksNames.explore,
                apiRanks.ranks.explore,
                apiRanks.progress.explore
            )
            val cqcRank = CommanderRank(
                apiRanks.ranksNames.cqc,
                apiRanks.ranks.cqc,
                apiRanks.progress.cqc
            )
            val mercenaryRank = CommanderRank(
                apiRanks.ranksNames.mercenary,
                apiRanks.ranks.mercenary,
                apiRanks.progress.mercenary
            )
            val exobiologistRank = CommanderRank(
                apiRanks.ranksNames.exobiologist,
                apiRanks.ranks.exobiologist,
                apiRanks.progress.exobiologist
            )
            val federationRank = CommanderRank(
                apiRanks.ranksNames.federation,
                apiRanks.ranks.federation,
                apiRanks.progress.federation
            )
            val empireRank = CommanderRank(
                apiRanks.ranksNames.empire,
                apiRanks.ranks.empire,
                apiRanks.progress.empire
            )

            ProxyResult(
                data = CommanderRanks(
                    combatRank,
                    tradeRank,
                    exploreRank,
                    cqcRank,
                    exobiologistRank,
                    mercenaryRank,
                    federationRank,
                    empireRank
                ), error = null
            )
        } catch (t: Throwable) {
            ProxyResult(data = null, error = t)
        }
    }

    override suspend fun getCredits(): ProxyResult<CommanderCredits> {
        return try {
            val apiCredits = edsmRetrofit.getCredits(apiKey, commanderName)

            ProxyResult(
                data = CommanderCredits(
                    apiCredits.credits[0].balance,
                    apiCredits.credits[0].loan,
                ), error = null
            )
        } catch (t: Throwable) {
            ProxyResult(data = null, error = t)
        }
    }

    override suspend fun getFleet(): ProxyResult<CommanderFleet> {
        throw UnsupportedOperationException("EDSM cannot fetch fleet")
    }

    override suspend fun getCurrentLoadout(): ProxyResult<CommanderLoadout> {
        throw UnsupportedOperationException("EDSM cannot fetch loadout")
    }
    
    override suspend fun getAllLoadouts(): ProxyResult<CommanderLoadouts> {
        throw UnsupportedOperationException("EDSM cannot fetch loadouts")
    }
}