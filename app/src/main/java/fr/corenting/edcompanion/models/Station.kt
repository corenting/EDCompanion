package fr.corenting.edcompanion.models

import fr.corenting.edcompanion.models.apis.EDApi.StationResponse
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.Instant

data class Station(val name: String, val lastShipyardUpdate: Instant?,
                   val distanceToStar: Int, val maxLandingPad: String?, val isPlanetary: Boolean,
                   val type: String, val systemName: String) {

    companion object {
        fun fromStationResponse(res: StationResponse): Station {
            var lastShipyardUpdate: Instant? = null
            if (res.LastShipyardUpdate != null) {
                lastShipyardUpdate =  DateTimeUtils.toInstant(res.LastShipyardUpdate)
            }
            return Station(res.Name, lastShipyardUpdate,
                    res.DistanceToArrival, res.MaxLandingPad, res.IsPlanetary, res.Type,
                    res.SystemName
            )
        }
    }
}