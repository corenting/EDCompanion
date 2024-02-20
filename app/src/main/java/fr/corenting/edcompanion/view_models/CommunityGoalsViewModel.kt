package fr.corenting.edcompanion.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.corenting.edcompanion.models.ProxyResult
import fr.corenting.edcompanion.models.events.CommunityGoals
import fr.corenting.edcompanion.network.CommunityGoalsNetwork
import kotlinx.coroutines.launch

class CommunityGoalsViewModel(application: Application) : AndroidViewModel(application) {
    private val communityGoalsResult = MutableLiveData<ProxyResult<CommunityGoals>>()

    fun fetchCommunityGoals() {
        viewModelScope.launch {
            communityGoalsResult.postValue(
                CommunityGoalsNetwork.getCommunityGoals(getApplication())
            )
        }
    }

    fun getCommunityGoals(): LiveData<ProxyResult<CommunityGoals>> {
        return communityGoalsResult
    }
}