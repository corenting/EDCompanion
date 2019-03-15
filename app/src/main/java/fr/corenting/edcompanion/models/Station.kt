package fr.corenting.edcompanion.models

import fr.corenting.edcompanion.models.apis.EDApi.StationResponse
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.Instant

data class Station(val name: String, val lastShipyardUpdate: Instant?,
                   val distanceToStar: Int, val maxLandingPad: String, val isPlanetary: Boolean,
                   val type: String, val shipsSold: List<String>, val systemName: String) {

    companion object {
        fun fromStationResponse(res: StationResponse): Station {
            return Station(res.Name, DateTimeUtils.toInstant(res.LastShipyardUpdate),
                    res.DistanceToStar, res.MaxLandingPad, res.IsPlanetary, res.Type, res.ShipsSold,
                    res.System.Name
            )
        }
    }
}