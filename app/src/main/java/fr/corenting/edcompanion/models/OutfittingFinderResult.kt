package fr.corenting.edcompanion.models

import fr.corenting.edcompanion.models.apis.EDAPIV4.OutfittingFinderResponse
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.Instant

data class OutfittingFinderResult(
    val landingPad: String,
    val station: String,
    val system: String,
    val isPlanetary: Boolean,
    val isSettlement: Boolean,
    val isFleetCarrier: Boolean,
    val distanceToArrival: Float,
    val distanceFromReferenceSystem: Float,
    val lastOutfittingUpdate: Instant,
) {
    companion object {
        fun fromOutfittingFinderResponse(res: OutfittingFinderResponse): OutfittingFinderResult {
            return OutfittingFinderResult(
                res.MaxLandingPad,
                res.Name,
                res.SystemName,
                res.IsPlanetary,
                res.IsSettlement,
                res.IsFleetCarrier,
                res.DistanceToArrival,
                res.DistanceFromReferenceSystem,
                DateTimeUtils.toInstant(res.LastOutfittingUpdate),
            )
        }
    }
}
