package fr.corenting.edcompanion.models

import fr.corenting.edcompanion.models.apis.EDAPIV4.CommodityBestPricesResponse
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.Instant

data class CommodityBestPricesStationResult(
    val collectedAt: Instant,
    val quantity: Long,
    val price: Long,
    val priceDifferencePercentage: Int,
    val stationName: String,
    val systemName: String,
    val stationType: String,
    val distanceFromReferenceSystem: Float,
    val distanceFromArrival: Float,
    val maxLandingPadSize: String,
    val stationIsPlanetary: Boolean,
    val stationIsSettlement: Boolean,
) {
    companion object {
        fun fromEDApiCommodityBestPricesStation(res: CommodityBestPricesResponse.CommodityBestPricesStationResponse):
                CommodityBestPricesStationResult {
            return CommodityBestPricesStationResult(
                DateTimeUtils.toInstant(res.LastMarketUpdate),
                res.Quantity,
                res.Price,
                res.PricePercentageDifference,
                res.Name,
                res.SystemName,
                res.Type,
                res.DistanceFromReferenceSystem,
                res.DistanceToArrival,
                res.MaxLandingPad,
                res.IsPlanetary,
                res.IsSettlement
            )
        }
    }
}

