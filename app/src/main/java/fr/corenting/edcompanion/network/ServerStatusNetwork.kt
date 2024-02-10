package fr.corenting.edcompanion.network

import android.content.Context
import fr.corenting.edcompanion.models.ProxyResult
import fr.corenting.edcompanion.models.ServerStatus
import fr.corenting.edcompanion.singletons.RetrofitSingleton

object ServerStatusNetwork {

    suspend fun getStatus(ctx: Context): ProxyResult<ServerStatus> {

        val edApiRetrofit = RetrofitSingleton.getInstance()
            .getEdApiV4Retrofit(ctx.applicationContext)

        return try {
            val serverStatus = edApiRetrofit.getGameServerHealth()
            ProxyResult(
                ServerStatus(serverStatus.Status)
            )
        } catch (t: Throwable) {
            ProxyResult(null, t)
        }
    }
}