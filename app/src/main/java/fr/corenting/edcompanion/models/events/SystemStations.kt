package fr.corenting.edcompanion.models.events

import fr.corenting.edcompanion.models.Station

data class SystemStations(val success: Boolean, val stations: List<Station>)
