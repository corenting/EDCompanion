package fr.corenting.edcompanion.models.events

import fr.corenting.edcompanion.models.CommodityDetailsStationResult

data class CommodityDetailsBuy(val success: Boolean,
                               val stations: List<CommodityDetailsStationResult>)
