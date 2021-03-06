package fr.corenting.edcompanion.models

import fr.corenting.edcompanion.models.apis.EDApi.FactionHistoryResponse
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.Instant

data class FactionHistory(val system: String, val state: String, val updateDate: Instant,
                          val influence: Float) {
    companion object {
        fun fromFactionHistoryResponse(res: FactionHistoryResponse): FactionHistory {
            return FactionHistory(res.System, res.State, DateTimeUtils.toInstant(res.UpdatedAt),
                    res.Influence)
        }
    }
}
