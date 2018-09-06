package fr.corenting.edcompanion.models

import fr.corenting.edcompanion.models.apis.EDApi.FactionResponse
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.Instant

data class Faction(val name: String, val state: String, val updateDate: Instant,
                   val influence: Float) {
    companion object {
        fun fromFactionResponse(res: FactionResponse): Faction {
            return Faction(res.Name, res.State, DateTimeUtils.toInstant(res.UpdatedAt),
                    res.Influence)
        }
    }
}
