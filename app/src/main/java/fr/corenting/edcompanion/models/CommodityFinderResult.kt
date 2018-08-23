package fr.corenting.edcompanion.models

import fr.corenting.edcompanion.models.apis.EDApi.CommodityFinderResponse
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.Instant

data class CommodityFinderResult(val buyPrice: Long, val landingPad: String, val station: String,
                                 val system: String, val isPermitRequired: Boolean,
                                 val isPlanetary: Boolean, val stock: Long, val distanceToStar: Int,
                                 val distance: Float, val lastPriceUpdate: Instant,
                                 val priceDifferenceFromAverage: Int) {
    companion object {
        fun fromCommodityFinderResponse(res: CommodityFinderResponse): CommodityFinderResult {
            return CommodityFinderResult(
                    res.BuyPrice,
                    res.Station.MaxLandingPad,
                    res.Station.Name,
                    res.Station.System.Name,
                    res.Station.System.PermitRequired,
                    res.Station.IsPlanetary,
                    res.Stock,
                    res.DistanceToStar,
                    res.Distance,
                    DateTimeUtils.toInstant(res.LastPriceUpdate),
                    res.PriceDifferencePercentage)
        }
    }
}
