package fr.corenting.edcompanion.models.events

data class Ranks(val success: Boolean, val combat: Rank?, val trade: Rank?, val explore: Rank?,
                 val cqc: Rank?, val federation: Rank?, val empire: Rank?) {

    data class Rank(val name: String, val value: Int, val progress: Int)
}
