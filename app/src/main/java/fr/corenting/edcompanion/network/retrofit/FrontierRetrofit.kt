package fr.corenting.edcompanion.network.retrofit

import fr.corenting.edcompanion.models.apis.Frontier.FrontierProfileResponse
import okhttp3.ResponseBody
import retrofit2.http.GET

interface FrontierRetrofit {
    @GET("profile")
    suspend fun  getProfileRaw(): ResponseBody

    @GET("profile")
    suspend fun  getProfile(): FrontierProfileResponse
}