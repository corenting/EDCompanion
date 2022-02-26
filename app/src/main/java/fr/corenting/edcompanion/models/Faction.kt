package fr.corenting.edcompanion.models

import fr.corenting.edcompanion.models.apis.EDAPIV4.SystemResponse
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.Instant

data class Faction(val name: String, val state: String, val government: String,
                   val allegiance: String, val updateDate: Instant, val influence: Float,
                   val happiness: String) {
    companion object {
        fun fromFactionResponse(res: SystemResponse.SystemFactionResponse): Faction {
            return Faction(res.Name, res.State, res.Government, res.Allegiance,
                    DateTimeUtils.toInstant(res.UpdatedAt), res.Influence, res.Happiness)
        }
    }
}
