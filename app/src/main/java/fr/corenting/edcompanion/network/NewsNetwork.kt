package fr.corenting.edcompanion.network

import android.content.Context
import fr.corenting.edcompanion.models.NewsArticle
import fr.corenting.edcompanion.models.NewsArticle.Companion.fromNewsArticleResponse
import fr.corenting.edcompanion.models.ProxyResult
import fr.corenting.edcompanion.models.events.News
import fr.corenting.edcompanion.singletons.RetrofitSingleton

object NewsNetwork {

    suspend fun getNews(ctx: Context, initialLanguage: String): ProxyResult<News> {
        // Set default language if none provided
        var language = initialLanguage
        if (language == "") {
            language = "en"
        }
        val retrofit = RetrofitSingleton.getInstance()
            .getEdApiV4Retrofit(ctx.applicationContext)

        try {
            val newsRet = retrofit.getNews(language)

            val news = try {
                val articles: MutableList<NewsArticle> = ArrayList()
                for (item in newsRet) {
                    articles.add(fromNewsArticleResponse(item))
                }
                News(articles)
            } catch (e: Exception) {
                News(ArrayList())
            }
            return ProxyResult(news, null)
        } catch (t: Throwable) {
            return ProxyResult(null, t)
        }
    }

    suspend fun getGalnetNews(ctx: Context, initialLanguage: String): ProxyResult<News> {
        // Set default language if none provided
        var language = initialLanguage
        if (language == "") {
            language = "en"
        }
        val retrofit = RetrofitSingleton.getInstance()
            .getEdApiV4Retrofit(ctx.applicationContext)

        try {
            val newsRet = retrofit.getGalnetNews(language)

            val news = try {
                val articles: MutableList<NewsArticle> = ArrayList()
                for (item in newsRet) {
                    articles.add(fromNewsArticleResponse(item))
                }
                News(articles)
            } catch (e: Exception) {
                News(ArrayList())
            }
            return ProxyResult(news, null)
        } catch (t: Throwable) {
            return ProxyResult(null, t)
        }
    }
}