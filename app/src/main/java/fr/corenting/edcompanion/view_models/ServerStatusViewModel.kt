package fr.corenting.edcompanion.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.corenting.edcompanion.models.ProxyResult
import fr.corenting.edcompanion.models.ServerStatus
import fr.corenting.edcompanion.network.ServerStatusNetwork
import kotlinx.coroutines.launch

class ServerStatusViewModel(application: Application) : AndroidViewModel(application) {
    private val serverStatus = MutableLiveData<ProxyResult<ServerStatus>>()

    fun fetchServerStatus() {
        viewModelScope.launch {
            serverStatus.postValue(ServerStatusNetwork.getStatus(getApplication()))
        }
    }

    fun getServerStatus(): LiveData<ProxyResult<ServerStatus>> {
        return serverStatus
    }
}