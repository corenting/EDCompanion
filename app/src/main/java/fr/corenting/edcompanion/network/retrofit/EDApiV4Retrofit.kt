package fr.corenting.edcompanion.network.retrofit

import fr.corenting.edcompanion.models.apis.EDAPIV4.CommodityBestPricesResponse
import fr.corenting.edcompanion.models.apis.EDAPIV4.CommodityFinderResponse
import fr.corenting.edcompanion.models.apis.EDAPIV4.CommodityWithPriceResponse
import fr.corenting.edcompanion.models.apis.EDAPIV4.CommunityGoalsResponse
import fr.corenting.edcompanion.models.apis.EDAPIV4.DistanceResponse
import fr.corenting.edcompanion.models.apis.EDAPIV4.GameServerHealthResponse
import fr.corenting.edcompanion.models.apis.EDAPIV4.NewsArticleResponse
import fr.corenting.edcompanion.models.apis.EDAPIV4.NewsArticleResponse.SystemHistoryResponse
import fr.corenting.edcompanion.models.apis.EDAPIV4.OutfittingFinderResponse
import fr.corenting.edcompanion.models.apis.EDAPIV4.ShipFinderResponse
import fr.corenting.edcompanion.models.apis.EDAPIV4.StationResponse
import fr.corenting.edcompanion.models.apis.EDAPIV4.SystemResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EDApiV4Retrofit {
    // TODO : convert to suspend/viewmodel etc...
    @GET("ships/typeahead")
    fun getShipsTypeAhead(@Query("input_text") shipName: String): Call<List<String>>

    @GET("galnet")
    suspend fun getGalnetNews(@Query("lang") language: String): List<NewsArticleResponse>

    @GET("news")
    suspend fun getNews(@Query("lang") language: String): List<NewsArticleResponse>

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

    @GET("community_goals")
    suspend fun getCommunityGoals(): List<CommunityGoalsResponse>

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
    fun getCommodityPrice(@Path("commodity") commodityName: String): Call<CommodityWithPriceResponse>

    // TODO : convert to suspend/viewmodel etc...
    @GET("commodities/{commodity}/best_prices")
    fun getCommodityBestPrices(@Path("commodity") commodityName: String): Call<CommodityBestPricesResponse>

    // TODO : convert to suspend/viewmodel etc...
    @GET("outfitting/typeahead")
    fun getOutfittingTypeAhead(@Query("input_text") outfittingName: String): Call<List<String>>

    // TODO : convert to suspend/viewmodel etc...
    @GET("commodities/find")
    fun findCommodity(
        @Query("reference_system") system: String,
        @Query("commodity") commodity: String,
        @Query("min_landing_pad_size") minLandingPad: String,
        @Query("min_quantity") minQuantity: Int,
        @Query("mode") searchMode: String
    ): Call<List<CommodityFinderResponse>>

    // TODO : convert to suspend/viewmodel etc...
    @GET("outfitting/find")
    fun findOutfitting(
        @Query("reference_system") system: String,
        @Query("outfitting") outfitting: String,
        @Query("min_landing_pad_size") minLandingPad: String,
    ): Call<List<OutfittingFinderResponse>>

    @GET("game_server_health")
    suspend fun getGameServerHealth(): GameServerHealthResponse
}