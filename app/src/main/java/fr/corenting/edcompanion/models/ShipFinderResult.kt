package fr.corenting.edcompanion.models

import fr.corenting.edcompanion.models.apis.EDApi.ShipFinderResponse
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.Instant

data class ShipFinderResult(val distance: Float, val DistanceToStar: Int,
                            val lastShipyardUpdate: Instant, val maxLandingPad: String,
                            val stationName: String, val systemName: String,
                            val isPermitRequired: Boolean, val isPlanetary: Boolean) {
    companion object {
        fun fromShipFinderResponse(res: ShipFinderResponse): ShipFinderResult {
            return ShipFinderResult(res.Distance, res.Station.DistanceToStar,
                    DateTimeUtils.toInstant(res.Station.LastShipyardUpdate),
                    res.Station.MaxLandingPad, res.Station.Name, res.Station.System.Name,
                    res.Station.System.PermitRequired, res.Station.IsPlanetary)
        }
    }
}

