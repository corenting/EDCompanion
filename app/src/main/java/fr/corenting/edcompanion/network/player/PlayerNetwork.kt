package fr.corenting.edcompanion.network.player;

import fr.corenting.edcompanion.models.*


interface  PlayerNetwork {
    fun isUsable(): Boolean
    fun getCommanderName(): String

    suspend fun getPosition(): ProxyResult<CommanderPosition>
    suspend fun getRanks(): ProxyResult<CommanderRanks>
    suspend fun getCredits(): ProxyResult<CommanderCredits>
    suspend fun getFleet(): ProxyResult<CommanderFleet>
}