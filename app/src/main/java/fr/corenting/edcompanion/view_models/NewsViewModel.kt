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

class NewsViewModel(application: Application) : AndroidViewModel(application) {
    private val newsResult = MutableLiveData<ProxyResult<News>>()

    fun fetchNews(language: String) {
        viewModelScope.launch {
            newsResult.postValue(
                NewsNetwork.getNews(getApplication(), language)
            )
        }
    }

    fun getNews(): LiveData<ProxyResult<News>> {
        return newsResult
    }
}