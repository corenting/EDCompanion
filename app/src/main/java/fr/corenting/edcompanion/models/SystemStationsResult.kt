package fr.corenting.edcompanion.models

import fr.corenting.edcompanion.models.apis.EDApi.SystemHistoryResponse

data class SystemStationsResult(val name: String, val history: List<FactionHistory>) {
    companion object {
        fun fromSystemHistoryResponse(res: SystemHistoryResponse): SystemStationsResult {
            val history = ArrayList<FactionHistory>()
            res.History.mapTo(history) { FactionHistory.fromFactionHistoryResponse(it) }
            return SystemStationsResult(res.Name, history)
        }
    }
}
