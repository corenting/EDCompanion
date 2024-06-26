package fr.corenting.edcompanion.network.player

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.models.CommanderCredits
import fr.corenting.edcompanion.models.CommanderFleet
import fr.corenting.edcompanion.models.CommanderLoadout
import fr.corenting.edcompanion.models.CommanderLoadoutWeapon
import fr.corenting.edcompanion.models.CommanderLoadouts
import fr.corenting.edcompanion.models.CommanderPosition
import fr.corenting.edcompanion.models.CommanderRank
import fr.corenting.edcompanion.models.CommanderRanks
import fr.corenting.edcompanion.models.ProxyResult
import fr.corenting.edcompanion.models.Ship
import fr.corenting.edcompanion.models.apis.Frontier.FrontierProfileResponse
import fr.corenting.edcompanion.models.apis.Frontier.FrontierProfileResponse.FrontierProfileCommanderRankResponse
import fr.corenting.edcompanion.models.exceptions.FrontierAuthNeededException
import fr.corenting.edcompanion.network.retrofit.FrontierRetrofit
import fr.corenting.edcompanion.singletons.RetrofitSingleton
import fr.corenting.edcompanion.utils.InternalNamingUtils
import fr.corenting.edcompanion.utils.SettingsUtils
import org.threeten.bp.Instant


class FrontierPlayer(val context: Context) : PlayerNetwork {

    private val frontierRetrofit: FrontierRetrofit =
        RetrofitSingleton.getInstance().getFrontierRetrofit(context.applicationContext)

    private var lastFetch: Instant = Instant.MIN

    private var cachedRanks: CommanderRanks? = null
    private var cachedCredits: CommanderCredits? = null
    private var cachedPosition: CommanderPosition? = null
    private var cachedFleet: CommanderFleet? = null
    private var cachedCurrentLoadout: CommanderLoadout? = null
    private var cachedAllLoadouts: CommanderLoadouts? = null


    override fun isUsable(): Boolean {
        return SettingsUtils.getBoolean(
            context, context.getString(R.string.settings_cmdr_frontier_enable), false
        )
    }

    override fun getCommanderName(): String {
        return context.getString(R.string.commander)

    }

    private fun shouldFetchNewData(): Boolean {
        synchronized(lastFetch) {
            val ret = when {
                lastFetch.isBefore(Instant.now().minusSeconds(60)) -> true
                else -> false
            }

            // Update last fetch date
            lastFetch = Instant.now()

            return ret
        }
    }

    private fun getRanksFromApiResponse(profileResponse: FrontierProfileResponse): CommanderRanks {
        val apiRanks: FrontierProfileCommanderRankResponse = profileResponse.Commander.Rank

        // Combat
        val combatRank = CommanderRank(
            context.resources.getStringArray(R.array.ranks_combat)[apiRanks.Combat],
            apiRanks.Combat,
            -1
        )

        // Trade
        val tradeRank = CommanderRank(
            context.resources.getStringArray(R.array.ranks_trade)[apiRanks.Trade],
            apiRanks.Trade,
            -1
        )

        // Explore
        val exploreRank = CommanderRank(
            context.resources.getStringArray(R.array.ranks_explorer)[apiRanks.Explore],
            apiRanks.Explore,
            -1
        )

        // CQC
        val cqcRank = CommanderRank(
            context.resources.getStringArray(R.array.ranks_cqc)[apiRanks.Cqc], apiRanks.Cqc, -1
        )

        // Merceneray
        val mercenaryRank = CommanderRank(
            context.resources.getStringArray(R.array.ranks_mercenary)[apiRanks.Mercenary],
            apiRanks.Mercenary,
            -1
        )

        // Exobiologist
        val exobiologistRank = CommanderRank(
            context.resources.getStringArray(R.array.ranks_exobiologist)[apiRanks.Exobiologist],
            apiRanks.Exobiologist,
            -1
        )

        // Federation
        val federationRank = CommanderRank(
            context.resources.getStringArray(R.array.ranks_federation)[apiRanks.Federation],
            apiRanks.Federation,
            -1
        )

        // Empire
        val empireRank = CommanderRank(
            context.resources.getStringArray(R.array.ranks_empire)[apiRanks.Empire],
            apiRanks.Empire,
            -1
        )

        return CommanderRanks(
            combatRank,
            tradeRank,
            exploreRank,
            cqcRank,
            exobiologistRank,
            mercenaryRank,
            federationRank,
            empireRank
        )
    }

    private suspend fun getProfile() {
        try {
            val apiResponse = frontierRetrofit.getProfileRaw()
            val rawResponse = JsonParser.parseString(apiResponse.string()).asJsonObject
            val profileResponse = Gson().fromJson(rawResponse, FrontierProfileResponse::class.java)

            cachedPosition = CommanderPosition(
                profileResponse.LastSystem.Name, false
            )
            cachedCredits =
                CommanderCredits(profileResponse.Commander.Credits, profileResponse.Commander.Debt)
            cachedRanks = getRanksFromApiResponse(profileResponse)
            cachedAllLoadouts = getAllLoadoutsFromApiResponse(rawResponse)
            cachedCurrentLoadout =
                getCurrentLoadoutFromApiResponse(rawResponse) // cannot get from the list as it has less informations
            cachedFleet = getFleetFromApiResponse(rawResponse)
        } catch (t: FrontierAuthNeededException) {
            lastFetch = Instant.MIN
            cachedCredits = null
            cachedRanks = null
            cachedFleet = null
            cachedCurrentLoadout = null
            cachedAllLoadouts = null
            throw t
        }
    }

    private fun getWeaponFromLoadoutResponse(
        slotsObject: JsonObject, weaponSlotName: String
    ): CommanderLoadoutWeapon? {
        try {
            if (!slotsObject.has(weaponSlotName)) {
                return null
            }

            val weaponObject = slotsObject.get(weaponSlotName).asJsonObject
            var magazineName: String? = null

            // If getting weapon for all loadouts the slots are not included in the response
            if (weaponObject.has("slots") && weaponObject.get("slots").asJsonObject.has("Magazine")) {
                val magazine = weaponObject.get("slots").asJsonObject.get("Magazine").asJsonObject
                magazineName = magazine.get("locName").asString
            }

            return CommanderLoadoutWeapon(
                name = weaponObject.get("locName").asString,
                magazineName = magazineName,
            )
        } catch (t: Throwable) {
            return null
        }
    }

    private fun getCurrentLoadoutFromApiResponse(profileResponse: JsonObject): CommanderLoadout {
        try {
            val loadoutElement = profileResponse.get("loadout")
            val slotsObject = loadoutElement.asJsonObject.get("slots").asJsonObject

            var loadoutName: String? = null
            if (loadoutElement.asJsonObject.has("name")) {
                loadoutName = loadoutElement.asJsonObject.get("name").asString
            }

            return CommanderLoadout(
                true,
                loadoutElement.asJsonObject.get("loadoutSlotId").asInt,
                loadoutName,
                loadoutElement.asJsonObject.get("suit").asJsonObject.get("locName").asString,
                getWeaponFromLoadoutResponse(slotsObject, "PrimaryWeapon1"),
                getWeaponFromLoadoutResponse(slotsObject, "PrimaryWeapon2"),
                getWeaponFromLoadoutResponse(slotsObject, "SecondaryWeapon")
            )

        } catch (t: Throwable) {
            return CommanderLoadout(
                false, -1, null, context.getString(R.string.unknown), null, null, null
            )
        }
    }

    private fun getLoadoutFromApiResponseItem(loadoutElement: JsonElement): CommanderLoadout? {
        try {
            val slotsObject = loadoutElement.asJsonObject.get("slots").asJsonObject

            var loadoutName: String? = null
            if (loadoutElement.asJsonObject.has("name")) {
                loadoutName = loadoutElement.asJsonObject.get("name").asString
            }

            return CommanderLoadout(
                true,
                loadoutElement.asJsonObject.get("loadoutSlotId").asInt,
                loadoutName,
                loadoutElement.asJsonObject.get("suit").asJsonObject.get("locName").asString,
                getWeaponFromLoadoutResponse(slotsObject, "PrimaryWeapon1"),
                getWeaponFromLoadoutResponse(slotsObject, "PrimaryWeapon2"),
                getWeaponFromLoadoutResponse(slotsObject, "SecondaryWeapon")
            )

        } catch (_: Throwable) {
        }
        return null
    }

    private fun getAllLoadoutsFromApiResponse(loadoutsElement: JsonObject): CommanderLoadouts {
        val loadouts = mutableListOf<CommanderLoadout>()

        // Sometimes the cAPI return an array, sometimes an object with indexes
        if (loadoutsElement.get("loadouts").isJsonObject) {
            for ((_, loadout) in loadoutsElement.get("loadouts").asJsonObject.entrySet()) {
                getLoadoutFromApiResponseItem(loadout)?.let { loadouts.add(it) }
            }
        } else {
            for (loadout in loadoutsElement.get("loadouts").asJsonArray) {
                getLoadoutFromApiResponseItem(loadout)?.let { loadouts.add(it) }
            }
        }

        return CommanderLoadouts(loadouts = loadouts)
    }

    private fun getFleetFromApiResponse(profileResponse: JsonObject): CommanderFleet {
        val currentShipId: Int =
            profileResponse.get("commander").asJsonObject.get("currentShipId").asInt

        // Sometimes the cAPI return an array, sometimes an object with indexes
        val responseList: MutableList<JsonElement> = ArrayList()
        if (profileResponse.get("ships").isJsonObject) {
            for ((_, value) in profileResponse.get("ships").asJsonObject.entrySet()) {
                responseList.add(value)
            }
        } else {
            for (ship in profileResponse.get("ships").asJsonArray) {
                responseList.add(ship)
            }
        }

        val shipsList: MutableList<Ship> = ArrayList()

        for (entry in responseList) {
            val rawShip = entry.asJsonObject
            var shipName: String? = null
            if (rawShip.has("shipName")) {
                shipName = rawShip["shipName"].asString
            }
            val value = rawShip["value"].asJsonObject
            val isCurrentShip = rawShip["id"].asInt == currentShipId
            val newShip = Ship(
                rawShip["id"].asInt,
                InternalNamingUtils.getShipName(rawShip["name"].asString),
                rawShip["name"].asString.lowercase(),
                shipName,
                rawShip["starsystem"].asJsonObject["name"].asString,
                rawShip["station"].asJsonObject["name"].asString,
                value["hull"].asLong,
                value["modules"].asLong,
                value["cargo"].asLong,
                value["total"].asLong,
                isCurrentShip
            )
            if (isCurrentShip) {
                shipsList.add(0, newShip)
            } else {
                shipsList.add(newShip)
            }
        }

        return CommanderFleet(shipsList)
    }

    override suspend fun getRanks(): ProxyResult<CommanderRanks> {
        if (!shouldFetchNewData() && cachedRanks != null) {
            return ProxyResult(cachedRanks)
        }

        return try {
            getProfile()
            ProxyResult(cachedRanks)
        } catch (t: Throwable) {
            ProxyResult(data = null, error = t)
        }
    }

    override suspend fun getCredits(): ProxyResult<CommanderCredits> {
        if (!shouldFetchNewData() && cachedCredits != null) {
            return ProxyResult(cachedCredits)
        }

        return try {
            getProfile()
            ProxyResult(cachedCredits)
        } catch (t: Throwable) {
            ProxyResult(data = null, error = t)
        }
    }

    override suspend fun getFleet(): ProxyResult<CommanderFleet> {
        if (!shouldFetchNewData() && cachedFleet != null) {
            return ProxyResult(cachedFleet)
        }

        return try {
            getProfile()
            ProxyResult(cachedFleet)
        } catch (t: Throwable) {
            ProxyResult(data = null, error = t)
        }
    }

    override suspend fun getCurrentLoadout(): ProxyResult<CommanderLoadout> {
        if (!shouldFetchNewData() && cachedCurrentLoadout != null) {
            return ProxyResult(cachedCurrentLoadout)
        }

        return try {
            getProfile()
            ProxyResult(cachedCurrentLoadout)
        } catch (t: Throwable) {
            ProxyResult(data = null, error = t)
        }
    }

    override suspend fun getAllLoadouts(): ProxyResult<CommanderLoadouts> {
        if (!shouldFetchNewData() && cachedAllLoadouts != null) {
            return ProxyResult(cachedAllLoadouts)
        }

        return try {
            getProfile()
            ProxyResult(cachedAllLoadouts)
        } catch (t: Throwable) {
            ProxyResult(data = null, error = t)
        }
    }

    override suspend fun getPosition(): ProxyResult<CommanderPosition> {
        if (!shouldFetchNewData() && cachedPosition != null) {
            return ProxyResult(cachedPosition)
        }

        return try {
            getProfile()
            ProxyResult(cachedPosition)
        } catch (t: Throwable) {
            ProxyResult(data = null, error = t)
        }
    }
}