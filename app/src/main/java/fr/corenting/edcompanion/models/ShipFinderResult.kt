package fr.corenting.edcompanion.models

import fr.corenting.edcompanion.models.apis.EDAPIV4.ShipFinderResponse
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.Instant

data class ShipFinderResult(
    val distance: Float,
    val DistanceToStar: Float,
    val lastShipyardUpdate: Instant,
    val maxLandingPad: String,
    val stationName: String,
    val systemName: String,
    val isPlanetary: Boolean,
    val isSettlement: Boolean,
    val isFleetCarrier: Boolean
) {
    companion object {
        fun fromShipFinderResponse(res: ShipFinderResponse): ShipFinderResult {
            return ShipFinderResult(
                res.DistanceFromReferenceSystem,
                res.DistanceToArrival,
                DateTimeUtils.toInstant(res.ShipyardUpdatedAt),
                res.MaxLandingPadSize,
                res.Name,
                res.SystemName,
                res.IsPlanetary,
                res.IsSettlement,
                res.IsFleetCarrier
            )
        }
    }
}

