package fr.corenting.edcompanion.models.events

import fr.corenting.edcompanion.models.CommodityDetailsResult

data class CommodityDetails(val success: Boolean, val commodityDetails: CommodityDetailsResult?)
