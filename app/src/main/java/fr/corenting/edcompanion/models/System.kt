package fr.corenting.edcompanion.models

import fr.corenting.edcompanion.models.apis.EDAPIV4.SystemResponse

data class System(
    val name: String, val isPermitRequired: Boolean, val allegiance: String?,
    val controllingFaction: String?, val government: String?, val population: Long?,
    val power: String?, val powerState: String?, val primaryEconomy: String?,
    val security: String?, val state: String?, val x: Float,
    val y: Float, val z: Float, val factions: List<Faction>
) {

    companion object {
        fun fromSystemResponse(res: SystemResponse): System {
            val factions = ArrayList<Faction>()
            res.Factions.mapTo(factions) { Faction.fromFactionResponse(it) }
            return System(
                res.Name, res.PermitRequired, res.Allegiance, res.ControllingFaction,
                res.Government, res.Population, res.Power, res.PowerState, res.PrimaryEconomy,
                res.Security, res.State, res.X, res.Y,
                res.Z, factions
            )
        }
    }
}
