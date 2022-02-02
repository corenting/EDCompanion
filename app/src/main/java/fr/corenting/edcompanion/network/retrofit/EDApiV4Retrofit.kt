package fr.corenting.edcompanion.network.retrofit

import fr.corenting.edcompanion.models.apis.EDAPIV4.DistanceResponse
import retrofit2.http.GET
import fr.corenting.edcompanion.models.apis.EDAPIV4.NewsArticleResponse
import fr.corenting.edcompanion.models.apis.EDAPIV4.CommunityGoalsResponse
import fr.corenting.edcompanion.models.apis.EDApi.StationResponse
import fr.corenting.edcompanion.models.apis.EDApi.SystemHistoryResponse
import retrofit2.Call
import retrofit2.http.Path
import retrofit2.http.Query

interface EDApiV4Retrofit {
    // TODO : convert to suspend/viewmodel etc...
    @GET("ships/typeahead")
    fun getShipsTypeAhead(@Query("input_text") shipName: String): Call<List<String>>

    @GET("galnet")
    fun getGalnetNews(@Query("lang") language: String): Call<List<NewsArticleResponse>>

    @GET("news")
    fun getNews(@Query("lang") language: String): Call<List<NewsArticleResponse>>

    @GET("systems/typeahead")
    fun getSystemsTypeAhead(@Query("input_text") systemName: String): Call<List<String>>

    @GET("systems/distance_calculator")
    suspend fun getSystemsDistance(
        @Query("first_system") firstSystemName: String,
        @Query("second_system") secondSystemName: String
    ): DistanceResponse

    // TODO : convert to suspend/viewmodel etc...
    @GET("systems/{system}/stations")
    fun getSystemStations(@Path("system") system: String): Call<List<StationResponse>>

    // TODO : convert to suspend/viewmodel etc...
    @GET("community_goals")
    fun getCommunityGoals(): Call<List<CommunityGoalsResponse>>

    // TODO : convert to suspend/viewmodel etc...
    @GET("commodities/typeahead")
    fun getCommoditiesTypeAhead(@Query("input_text") commodityName: String): Call<List<String>>

    // TODO : convert to suspend/viewmodel etc...
    @GET("systems/{system}/factions_history")
    fun getSystemHistory(@Path("system") systemName: String): Call<List<SystemHistoryResponse>>

}