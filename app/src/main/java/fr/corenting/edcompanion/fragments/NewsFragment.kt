package fr.corenting.edcompanion.fragments

import fr.corenting.edcompanion.adapters.NewsAdapter
import fr.corenting.edcompanion.models.events.News
import fr.corenting.edcompanion.network.NewsNetwork
import fr.corenting.edcompanion.utils.NotificationsUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class NewsFragment : AbstractListFragment<NewsAdapter>() {

    override fun getNewRecyclerViewAdapter(): NewsAdapter {
        return NewsAdapter(context, binding.recyclerView, false)
    }

    override fun getData() {
        NewsNetwork.getNews(context)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNewsEvent(news: News) {
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
        const val NEWS_FRAGMENT_TAG = "news_fragment"
    }
}
