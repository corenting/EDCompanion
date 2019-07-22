package fr.corenting.edcompanion.models.events

import fr.corenting.edcompanion.models.CommodityDetailsStationResult

data class CommodityDetailsSell(val success: Boolean,
                                val stations: List<CommodityDetailsStationResult>)
