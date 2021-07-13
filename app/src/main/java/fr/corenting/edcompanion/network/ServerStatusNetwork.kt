package fr.corenting.edcompanion.network

import android.content.Context
import fr.corenting.edcompanion.models.ProxyResult
import fr.corenting.edcompanion.models.ServerStatus
import fr.corenting.edcompanion.singletons.RetrofitSingleton

object ServerStatusNetwork {

    suspend fun getStatus(ctx: Context): ProxyResult<ServerStatus> {

        val edsmRetrofit = RetrofitSingleton.getInstance()
            .getEDSMRetrofit(ctx.applicationContext)

        return try {
            val edsmServerStatus = edsmRetrofit.getServerStatus()
            ProxyResult(
                ServerStatus(edsmServerStatus.Message)
            )
        } catch (t: Throwable) {
            ProxyResult(null, t)
        }
    }
}