package fr.corenting.edcompanion.network.retrofit

import fr.corenting.edcompanion.models.apis.Inara.InaraProfileRequestBody
import fr.corenting.edcompanion.models.apis.Inara.InaraProfileResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface InaraRetrofit {
    @POST("inapi/v1/")
    suspend fun getProfile(@Body body: InaraProfileRequestBody): InaraProfileResponse
}