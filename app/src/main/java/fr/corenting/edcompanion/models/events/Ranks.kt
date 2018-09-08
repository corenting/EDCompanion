package fr.corenting.edcompanion.models.events

data class Ranks @JvmOverloads
constructor(val success: Boolean, val combat: Rank? = null, val trade: Rank? = null,
            val explore: Rank? = null, val cqc: Rank? = null, val federation: Rank? = null,
            val empire: Rank? = null) {

    data class Rank(val name: String, val value: Int, val progress: Int)
}
