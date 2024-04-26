package fr.corenting.edcompanion.models

import fr.corenting.edcompanion.models.apis.EDAPIV4.CommodityFinderResponse
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.Instant

data class CommodityFinderResult(
    val price: Long,
    val landingPad: String,
    val station: String,
    val system: String,
    val isPlanetary: Boolean,
    val isSettlement: Boolean,
    val isFleetCarrier: Boolean,
    val quantity: Long,
    val distanceToArrival: Float,
    val distanceFromReferenceSystem: Float,
    val lastMarketUpdate: Instant,
    val pricePercentageDifference: Int
) {
    companion object {
        fun fromCommodityFinderResponse(res: CommodityFinderResponse): CommodityFinderResult {
            return CommodityFinderResult(
                res.Price,
                res.MaxLandingPad,
                res.Name,
                res.SystemName,
                res.IsPlanetary,
                res.IsSettlement,
                res.IsFleetCarrier,
                res.Quantity,
                res.DistanceToArrival,
                res.DistanceFromReferenceSystem,
                DateTimeUtils.toInstant(res.LastMarketUpdate),
                res.PricePercentageDifference
            )
        }
    }
}
