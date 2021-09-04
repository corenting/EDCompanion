package fr.corenting.edcompanion.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.corenting.edcompanion.models.ProxyResult
import fr.corenting.edcompanion.models.SystemsDistance
import fr.corenting.edcompanion.network.DistanceCalculatorNetwork
import kotlinx.coroutines.launch

class DistanceCalculatorViewModel(application: Application) : AndroidViewModel(application) {
    private val distanceCalculationResult = MutableLiveData<ProxyResult<SystemsDistance>>()

    fun computeDistanceBetweenSystems(firstSystemName: String, secondSystemName: String) {
        viewModelScope.launch {
            distanceCalculationResult.postValue(
                DistanceCalculatorNetwork.getDistance(
                    getApplication(),
                    firstSystemName,
                    secondSystemName
                )
            )
        }
    }

    fun getDistanceBetweenSystemsResult(): LiveData<ProxyResult<SystemsDistance>> {
        return distanceCalculationResult
    }
}