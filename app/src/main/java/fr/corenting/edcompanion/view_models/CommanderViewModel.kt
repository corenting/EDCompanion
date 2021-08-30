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
        if (edsmPlayer.isUsable()) {
            viewModelScope.launch {
                credits.postValue(edsmPlayer.getCredits())
            }
        } else if (frontierPlayer.isUsable()) {
            viewModelScope.launch {
                credits.postValue(frontierPlayer.getCredits())
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
        if (edsmPlayer.isUsable()) {
            viewModelScope.launch {
                val newPosition = edsmPlayer.getPosition()
                position.postValue(newPosition)
                updateCachedPosition(newPosition)
            }
        } else if (frontierPlayer.isUsable()) {
            viewModelScope.launch {
                val newPosition = frontierPlayer.getPosition()
                position.postValue(newPosition)
                updateCachedPosition(newPosition)
            }
        }


    }

    fun getPosition(): LiveData<ProxyResult<CommanderPosition>> {
        return position
    }

    fun fetchRanks() {
        if (edsmPlayer.isUsable()) {
            viewModelScope.launch {
                ranks.postValue(edsmPlayer.getRanks())
            }
        } else if (inaraPlayer.isUsable()) {
            viewModelScope.launch {
                ranks.postValue(inaraPlayer.getRanks())
            }
        } else if (frontierPlayer.isUsable()) {
            viewModelScope.launch {
                ranks.postValue(frontierPlayer.getRanks())
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