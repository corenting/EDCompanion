package fr.corenting.edcompanion.models

import android.os.Parcelable
import fr.corenting.edcompanion.models.apis.EDApi.NewsArticleResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsArticle(val title: String, val content: String, val picture: String?,
                       val dateTimestamp: Long) : Parcelable {
    companion object {
        fun fromNewsArticleResponse(res: NewsArticleResponse): NewsArticle {
            return NewsArticle(res.Title, res.Content, res.Picture, res.Timestamp)
        }
    }
}
