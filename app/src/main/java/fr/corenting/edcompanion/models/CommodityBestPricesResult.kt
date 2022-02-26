package fr.corenting.edcompanion.models

data class CommodityBestPricesResult(
    val bestStationsToBuy: List<CommodityBestPricesStationResult>,
    val bestStationsToSell: List<CommodityBestPricesStationResult>
)

