package fr.corenting.edcompanion.network.player

import android.content.Context
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.models.*
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
            SettingsUtils.getBoolean(context, context.getString(R.string.settings_cmdr_edsm_enable))
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
        throw UnsupportedOperationException("EDSM cannot fetch fleet informations")
    }
}