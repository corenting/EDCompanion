package fr.corenting.edcompanion.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.corenting.edcompanion.models.*
import fr.corenting.edcompanion.network.player.EDSMPlayer
import fr.corenting.edcompanion.network.player.FrontierPlayer
import fr.corenting.edcompanion.network.player.InaraPlayer
import fr.corenting.edcompanion.utils.CommanderUtils
import kotlinx.coroutines.launch

class CommanderViewModel(application: Application) : AndroidViewModel(application) {
    private val edsmPlayer: EDSMPlayer = EDSMPlayer(application)
    private val inaraPlayer: InaraPlayer = InaraPlayer(application)
    private val frontierPlayer: FrontierPlayer = FrontierPlayer(application)

    private val credits = MutableLiveData<ProxyResult<CommanderCredits>>()
    private val position = MutableLiveData<ProxyResult<CommanderPosition>>()
    private val ranks = MutableLiveData<ProxyResult<CommanderRanks>>()
    private val fleet = MutableLiveData<ProxyResult<CommanderFleet>>()

    fun fetchCredits() {
        val servicesToTries = listOf(edsmPlayer, frontierPlayer)

        viewModelScope.launch {
            for (service in servicesToTries) {
                val retValue = service.getCredits()

                if (retValue.error == null && retValue.data != null) {
                    credits.postValue(retValue)
                }
            }
        }
    }

    fun getCredits(): LiveData<ProxyResult<CommanderCredits>> {
        return credits
    }

    private fun updateCachedPosition(newPosition: ProxyResult<CommanderPosition>) {
        if (newPosition.data != null && newPosition.error == null) {
            CommanderUtils.setCachedCurrentCommanderPosition(
                getApplication(),
                newPosition.data.systemName
            )
        }
    }

    fun fetchPosition() {
        val servicesToTries = listOf(edsmPlayer, frontierPlayer)

        viewModelScope.launch {
            for (service in servicesToTries) {
                val retValue = service.getPosition()

                if (retValue.error == null && retValue.data != null) {
                    position.postValue(retValue)
                    updateCachedPosition(retValue)

                }
            }
        }
    }

    fun getPosition(): LiveData<ProxyResult<CommanderPosition>> {
        return position
    }

    fun fetchRanks() {
        val servicesToTries = listOf(edsmPlayer, inaraPlayer, frontierPlayer)

        viewModelScope.launch {
            for (service in servicesToTries) {
                val retValue = service.getRanks()

                if (retValue.error == null && retValue.data != null) {
                    ranks.postValue(retValue)
                }
            }
        }
    }

    fun getRanks(): LiveData<ProxyResult<CommanderRanks>> {
        return ranks
    }

    fun fetchFleet() {
        if (frontierPlayer.isUsable()) {
            viewModelScope.launch {
                fleet.postValue(frontierPlayer.getFleet())
            }
        }
    }

    fun getFleet(): LiveData<ProxyResult<CommanderFleet>> {
        return fleet
    }
}