package fr.corenting.edcompanion.models.events

import fr.corenting.edcompanion.models.CommodityBestPricesStationResult

data class CommodityBestPrices(
    val success: Boolean,
    val bestStationsToBuy: List<CommodityBestPricesStationResult>,
    val bestStationsToSell: List<CommodityBestPricesStationResult>,
)