package fr.corenting.edcompanion.models

data class CommanderRanks constructor(
    val combat: CommanderRank,
    val trade: CommanderRank,
    val explore: CommanderRank,
    val cqc: CommanderRank,
    val federation: CommanderRank,
    val empire: CommanderRank
)
