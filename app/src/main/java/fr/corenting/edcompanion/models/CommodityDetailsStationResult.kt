package fr.corenting.edcompanion.models

import fr.corenting.edcompanion.models.apis.EDApi.CommodityDetailsResponse
import java.util.*

data class CommodityDetailsStationResult(val collectedAt: Date, val demand: Long?, val supply: Long?,
                                         val buyPrice: Long, val sellPrice: Long,
                                         val priceDifferencePercentage: Int, val station: Station) {
    companion object {
        fun fromEDApiCommodityDetailsStations(res: CommodityDetailsResponse.CommodityDetailsStationResponse):
                CommodityDetailsStationResult {
            return CommodityDetailsStationResult(res.CollectedAt, res.Demand, res.Supply, res.BuyPrice,
                    res.SellPrice, res.PriceDifferencePercentage,
                    Station.fromStationResponse(res.Station))
        }
    }
}

