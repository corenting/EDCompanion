package fr.corenting.edcompanion.models

import fr.corenting.edcompanion.models.apis.EDApi.CommodityDetailsResponse
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.Instant
import java.util.*

data class CommodityDetailsStationResult(val collectedAt: Instant, val demand: Long?, val supply: Long?,
                                         val buyPrice: Long, val sellPrice: Long,
                                         val priceDifferencePercentage: Int, val station: Station) {
    companion object {
        fun fromEDApiCommodityDetailsStations(res: CommodityDetailsResponse.CommodityDetailsStationResponse):
                CommodityDetailsStationResult {
            return CommodityDetailsStationResult(DateTimeUtils.toInstant(res.CollectedAt),
                    res.Demand, res.Supply, res.BuyPrice,
                    res.SellPrice, res.PriceDifferencePercentage,
                    Station.fromStationResponse(res.Station))
        }
    }
}

