package fr.corenting.edcompanion.models.events

import fr.corenting.edcompanion.models.Ship

data class Fleet(val success: Boolean, val ships: List<Ship>)
