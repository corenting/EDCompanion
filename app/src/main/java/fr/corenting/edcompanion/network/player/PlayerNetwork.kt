package fr.corenting.edcompanion.network.player;

import fr.corenting.edcompanion.models.ProxyResult;
import fr.corenting.edcompanion.models.events.CommanderPosition;
import fr.corenting.edcompanion.models.events.Credits;
import fr.corenting.edcompanion.models.events.Fleet;
import fr.corenting.edcompanion.models.events.Ranks;

interface  PlayerNetwork {
    fun isUsable();

    fun getCommanderPosition(): ProxyResult<CommanderPosition>
    fun getRanks(): ProxyResult<Ranks>
    fun getCredits(): ProxyResult<Credits>
    fun getFleet(): ProxyResult<Fleet>
}