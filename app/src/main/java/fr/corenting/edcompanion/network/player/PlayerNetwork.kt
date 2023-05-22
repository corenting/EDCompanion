package fr.corenting.edcompanion.network.player

import fr.corenting.edcompanion.models.CommanderCredits
import fr.corenting.edcompanion.models.CommanderFleet
import fr.corenting.edcompanion.models.CommanderPosition
import fr.corenting.edcompanion.models.CommanderRanks
import fr.corenting.edcompanion.models.ProxyResult


interface PlayerNetwork {
    fun isUsable(): Boolean
    fun getCommanderName(): String

    suspend fun getPosition(): ProxyResult<CommanderPosition>
    suspend fun getRanks(): ProxyResult<CommanderRanks>
    suspend fun getCredits(): ProxyResult<CommanderCredits>
    suspend fun getFleet(): ProxyResult<CommanderFleet>
}