package fr.corenting.edcompanion.fragments

import android.os.Bundle
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.adapters.NewsAdapter
import fr.corenting.edcompanion.models.events.GalnetNews
import fr.corenting.edcompanion.network.NewsNetwork
import fr.corenting.edcompanion.utils.NotificationsUtils
import fr.corenting.edcompanion.utils.SettingsUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class GalnetFragment : AbstractListFragment<NewsAdapter>() {
    private lateinit var currentLanguage: String

    private val newsLanguage: String
        get() = SettingsUtils.getString(context, getString(R.string.settings_news_lang))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentLanguage = newsLanguage
    }

    override fun onResume() {
        super.onResume()

        // Refresh if language changed
        if (currentLanguage != newsLanguage) {
            currentLanguage = newsLanguage
            getData()
        }
    }

    override fun getData() {
        NewsNetwork.getGalnetNews(context, currentLanguage)
    }

    override fun getNewRecyclerViewAdapter(): NewsAdapter {
        return NewsAdapter(context, binding.recyclerView, false)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNewsEvent(news: GalnetNews) {
        // Error case
        if (!news.success) {
            endLoading(true)
            NotificationsUtils.displayGenericDownloadErrorSnackbar(activity)
            return
        }

        endLoading(news.articles.isEmpty())
        recyclerViewAdapter.submitList(news.articles)
    }

    companion object {
        const val GALNET_FRAGMENT_TAG = "galnet_fragment"
    }
}
