package fr.corenting.edcompanion.models.events

import fr.corenting.edcompanion.models.SystemHistoryResult

data class SystemHistory(val success: Boolean, val history: List<SystemHistoryResult>)
