package fr.corenting.edcompanion.models

data class CommanderLoadout(
    val hasLoadout: Boolean,
    val loadoutId: Int,
    val loadoutName: String?,
    val suitName: String,
    val firstPrimaryWeapon: CommanderLoadoutWeapon?,
    val secondPrimaryWeapon: CommanderLoadoutWeapon?,
    val secondaryWeapon: CommanderLoadoutWeapon?,
)
