package fr.corenting.edcompanion.models.events

import fr.corenting.edcompanion.models.CommodityBestPricesStationResult

data class CommodityDetailsSell(val success: Boolean,
                                val stations: List<CommodityBestPricesStationResult>)
