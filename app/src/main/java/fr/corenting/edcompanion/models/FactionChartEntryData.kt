package fr.corenting.edcompanion.models

import org.threeten.bp.Instant

data class FactionChartEntryData(
    val name: String, val state: String, val updateDate: Instant,
    val influence: Float
)
