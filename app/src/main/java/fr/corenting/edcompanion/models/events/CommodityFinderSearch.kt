package fr.corenting.edcompanion.models.events

data class CommodityFinderSearch(
    val commodityName: String, val systemName: String,
    val landingPadSize: String, val stockOrDemand: Int,
    val isSellingMode: Boolean
)