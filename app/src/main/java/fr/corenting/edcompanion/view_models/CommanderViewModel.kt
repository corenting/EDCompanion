package fr.corenting.edcompanion.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.corenting.edcompanion.models.CommanderCredits
import fr.corenting.edcompanion.models.CommanderFleet
import fr.corenting.edcompanion.models.CommanderPosition
import fr.corenting.edcompanion.models.CommanderRanks
import fr.corenting.edcompanion.models.ProxyResult
import fr.corenting.edcompanion.models.exceptions.DataNotInitializedException
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

    fun clearCachedData() {
        // We set the values to a null result with a specific exception to mark it as not initialized
        viewModelScope.launch {
            credits.postValue(ProxyResult(null, DataNotInitializedException()))
            position.postValue(ProxyResult(null, DataNotInitializedException()))
            ranks.postValue(ProxyResult(null, DataNotInitializedException()))
            fleet.postValue(ProxyResult(null, DataNotInitializedException()))
        }
    }

    fun fetchCredits() {
        val servicesToTries = listOf(edsmPlayer, frontierPlayer)
        viewModelScope.launch {
            for ((index, service) in servicesToTries.withIndex()) {
                if (!service.isUsable()) {
                    continue
                }

                val retValue = service.getCredits()

                if (index == servicesToTries.size - 1 || (retValue.error == null && retValue.data != null)) {
                    credits.postValue(retValue)
                    break
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
            for ((index, service) in servicesToTries.withIndex()) {
                if (!service.isUsable()) {
                    continue
                }

                val retValue = service.getPosition()

                if (index == servicesToTries.size - 1 || (retValue.error == null && retValue.data != null)) {
                    position.postValue(retValue)
                    updateCachedPosition(retValue)
                    break
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
            for ((index, service) in servicesToTries.withIndex()) {
                if (!service.isUsable()) {
                    continue
                }

                val retValue = service.getRanks()
                if (index == servicesToTries.size - 1 || (retValue.error == null && retValue.data != null)) {
                    ranks.postValue(retValue)
                    break
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