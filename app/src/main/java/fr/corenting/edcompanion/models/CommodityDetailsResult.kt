package fr.corenting.edcompanion.models

import fr.corenting.edcompanion.models.apis.EDAPIV4.CommodityWithPriceResponse


data class CommodityDetailsResult(
    val name: String, val id: Long,
    val isRare: Boolean, val category: String,
    val averageSellPrice: Long,
    val averageBuyPrice: Long,
    val maximumSellPrice: Long, val minimumBuyPrice: Long
) {
    companion object {
        fun fromEDApiCommodityDetails(res: CommodityWithPriceResponse): CommodityDetailsResult {
            return CommodityDetailsResult(
                res.Commodity.Name, res.Commodity.Id, res.Commodity.IsRare, res.Commodity.Category,
                res.AverageSellPrice, res.AverageBuyPrice, res.MaximumSellPrice, res.MinimumBuyPrice
            )
        }
    }
}

