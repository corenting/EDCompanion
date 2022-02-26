package fr.corenting.edcompanion.models.events

import fr.corenting.edcompanion.models.CommodityBestPricesStationResult

data class CommodityDetailsBuy(val success: Boolean,
                               val stations: List<CommodityBestPricesStationResult>)
