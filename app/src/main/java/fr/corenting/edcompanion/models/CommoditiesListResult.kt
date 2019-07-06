package fr.corenting.edcompanion.models

import fr.corenting.edcompanion.models.apis.EDApi.CommodityResponse

data class CommoditiesListResult(val name: String, val id: Long, val averagePrice: Long) {
    companion object {
        fun fromEDApiCommodity(res: CommodityResponse): CommoditiesListResult {
            return CommoditiesListResult(res.Name, res.Id, res.AveragePrice)
        }
    }
}

