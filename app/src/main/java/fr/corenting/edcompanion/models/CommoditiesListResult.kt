package fr.corenting.edcompanion.models

import fr.corenting.edcompanion.models.apis.EDAPIV4.CommodityPricesResponse

data class CommoditiesListResult(
    val name: String, val id: Long, val averageBuyPrice: Long, val averageSellPrice: Long,
    val isRare: Boolean, val category: String
) {
    companion object {
        fun fromEDApiCommodityPrice(res: CommodityPricesResponse): CommoditiesListResult {
            return CommoditiesListResult(
                res.Commodity.Name,
                res.Commodity.Id,
                res.AverageBuyPrice,
                res.AverageSellPrice,
                res.Commodity.IsRare,
                res.Commodity.Category
            )
        }
    }
}

