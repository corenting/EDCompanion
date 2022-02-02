package fr.corenting.edcompanion.models

import fr.corenting.edcompanion.models.apis.EDApi.SystemHistoryResponse

data class SystemHistoryResult(val name: String, val history: List<FactionHistory>) {
    companion object {
        fun fromSystemHistoryResponse(res: SystemHistoryResponse): SystemHistoryResult {
            val history = ArrayList<FactionHistory>()
            res.History.mapTo(history) { FactionHistory.fromFactionHistoryResponse(it) }
            return SystemHistoryResult(res.FactionName, history)
        }
    }
}
