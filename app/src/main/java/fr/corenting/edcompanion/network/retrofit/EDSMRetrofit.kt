package fr.corenting.edcompanion.network.retrofit

import fr.corenting.edcompanion.models.apis.EDSM.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EDSMRetrofit {

    @GET("api-logs-v1/get-position")
    suspend fun getPosition(
        @Query("apiKey") apiKey: String?,
        @Query("commanderName") commanderName: String?
    ): EDSMPositionResponse

    @GET("api-commander-v1/get-credits")
    suspend fun getCredits(
        @Query("apiKey") apiKey: String?,
        @Query("commanderName") commanderName: String?
    ): EDSMCreditsResponse

    @GET("api-commander-v1/get-ranks")
    suspend fun getRanks(
        @Query("apiKey") apiKey: String?,
        @Query("commanderName") commanderName: String?
    ): EDSMRanksResponse

    @GET("api-status-v1/elite-server")
    suspend fun getServerStatus(): EDSMServerStatusResponse

    @GET("api-v1/systems")
    fun getSystems(
        @Query("systemName") systemName: String?,
        @Query("showId") showId: Int,
        @Query("showInformation") showInformation: Int,
        @Query("showPermit") showPermit: Int
    ): Call<List<EDSMSystemResponse>>
}