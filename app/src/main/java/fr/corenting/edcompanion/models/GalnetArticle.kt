package fr.corenting.edcompanion.models

import android.os.Parcelable
import fr.corenting.edcompanion.models.apis.EDApi.GalnetArticleResponse
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GalnetArticle(val title: String, val content: String, val picture: String?,
                         val dateTimestamp: Long) : Parcelable {
    companion object {
        fun fromGalnetArticleResponse(res: GalnetArticleResponse): GalnetArticle {
            return GalnetArticle(res.Title, res.Content.replace("<br />", "\n"),
                    res.Picture, res.Timestamp)
        }
    }
}
