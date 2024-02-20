package fr.corenting.edcompanion.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.corenting.edcompanion.models.ProxyResult
import fr.corenting.edcompanion.models.events.News
import fr.corenting.edcompanion.network.NewsNetwork
import kotlinx.coroutines.launch

class GalnetViewModel(application: Application) : AndroidViewModel(application) {
    private val galnetResult = MutableLiveData<ProxyResult<News>>()

    fun fetchGalnet(language: String) {
        viewModelScope.launch {
            galnetResult.postValue(
                NewsNetwork.getGalnetNews(getApplication(), language)
            )
        }
    }

    fun getGalnet(): LiveData<ProxyResult<News>> {
        return galnetResult
    }
}