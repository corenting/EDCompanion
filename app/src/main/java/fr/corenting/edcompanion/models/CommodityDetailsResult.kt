package fr.corenting.edcompanion.models

import fr.corenting.edcompanion.models.apis.EDApi.CommodityDetailsResponse

data class CommodityDetailsResult(val name: String, val id: Long, val averagePrice: Long,
                                  val isRare: Boolean, val category: NameId,
                                  val averageSellPrice: Double,
                                  val averageBuyPrice: Double, val maximumProfit: Long,
                                  val maximumSellPrice: Long, val minimumBuyPrice: Long,
                                  val maximumSellers: List<CommodityDetailsStationResult>,
                                  val minimumBuyers: List<CommodityDetailsStationResult>) {
    companion object {
        fun fromEDApiCommodityDetails(res: CommodityDetailsResponse): CommodityDetailsResult {

            val maxSellers = res.MaximumSellers.map {
                CommodityDetailsStationResult.fromEDApiCommodityDetailsStations(it)
            }

            val minBuyers = res.MinimumBuyers.map {
                CommodityDetailsStationResult.fromEDApiCommodityDetailsStations(it)
            }

            return CommodityDetailsResult(res.Name, res.Id, res.AveragePrice, res.IsRare,
                    res.Category, res.AverageSellPrice, res.AverageBuyPrice, res.MaximumProfit,
                    res.MaximumSellPrice, res.MinimumBuyPrice, maxSellers, minBuyers)
        }
    }
}

