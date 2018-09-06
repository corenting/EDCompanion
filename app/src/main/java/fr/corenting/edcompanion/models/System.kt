package fr.corenting.edcompanion.models

import fr.corenting.edcompanion.models.apis.EDApi.SystemResponse
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.Instant

data class System(val name: String, val isPermitRequired: Boolean, val allegiance: String,
                  val controllingFaction: String, val government: String, val population:Long,
                  val power:String, val powerState:String, val primaryEconomy:String,
                  val security: String, val state:String, val updateDate: Instant, val x:Float,
                  val y:Float, val z:Float, val factions: List<Faction>) {

    companion object {
        fun fromSystemResponse(res: SystemResponse): System {
            val factions = ArrayList<Faction>()
            res.Factions.mapTo(factions) { Faction.fromFactionResponse(it) }
            return System(res.Name, res.PermitRequired, res.Allegiance, res.ControllingFaction,
                    res.Government, res.Population, res.Power, res.PowerState, res.PrimaryEconomy,
                    res.Security, res.State, DateTimeUtils.toInstant(res.UpdatedAt), res.X, res.Y,
                    res.Z, factions)
        }
    }
}
