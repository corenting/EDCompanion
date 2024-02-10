package fr.corenting.edcompanion.network.retrofit

import fr.corenting.edcompanion.models.apis.EDAPIV4.*
import fr.corenting.edcompanion.models.apis.EDAPIV4.NewsArticleResponse.SystemHistoryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EDApiV4Retrofit {
    // TODO : convert to suspend/viewmodel etc...
    @GET("ships/typeahead")
    fun getShipsTypeAhead(@Query("input_text") shipName: String): Call<List<String>>

    // TODO : convert to suspend/viewmodel etc...
    @GET("galnet")
    fun getGalnetNews(@Query("lang") language: String): Call<List<NewsArticleResponse>>

    // TODO : convert to suspend/viewmodel etc...
    @GET("news")
    fun getNews(@Query("lang") language: String): Call<List<NewsArticleResponse>>

    // TODO : convert to suspend/viewmodel etc...
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

    // TODO : convert to suspend/viewmodel etc...
    @GET("ships/search")
    fun findShip(
        @Query("reference_system") system: String?,
        @Query("ship_model") ship: String?
    ): Call<List<ShipFinderResponse>>

    // TODO : convert to suspend/viewmodel etc...
    @GET("systems/{system}")
    fun getSystemDetails(@Path("system") system: String): Call<SystemResponse>

    // TODO : convert to suspend/viewmodel etc...
    @GET("commodities/prices")
    fun getCommoditiesWithPrice(@Query("filter") filter: String): Call<List<CommodityWithPriceResponse>>

    // TODO : convert to suspend/viewmodel etc...
    @GET("commodities/{commodity}/prices")
    fun getCommodityPrice(@Path("commodity") commodity_name: String): Call<CommodityWithPriceResponse>

    // TODO : convert to suspend/viewmodel etc...
    @GET("commodities/{commodity}/best_prices")
    fun getCommodityBestPrices(@Path("commodity") commodity_name: String): Call<CommodityBestPricesResponse>

    // TODO : convert to suspend/viewmodel etc...
    @GET("commodities/find")
    fun findCommodity(
        @Query("reference_system") system: String,
        @Query("commodity") commodity: String,
        @Query("min_landing_pad_size") minLandingPad: String,
        @Query("min_quantity") min_quantity: Int,
        @Query("mode") searchMode: String
    ): Call<List<CommodityFinderResponse>>

    @GET("game_server_health")
    suspend fun getGameServerHealth(): GameServerHealthResponse
}