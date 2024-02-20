package fr.corenting.edcompanion.network

import android.content.Context
import fr.corenting.edcompanion.models.CommunityGoal
import fr.corenting.edcompanion.models.ProxyResult
import fr.corenting.edcompanion.models.events.CommunityGoals
import fr.corenting.edcompanion.singletons.RetrofitSingleton

object CommunityGoalsNetwork {

    suspend fun getCommunityGoals(ctx: Context): ProxyResult<CommunityGoals> {
        val retrofit = RetrofitSingleton.getInstance()
            .getEdApiV4Retrofit(ctx.applicationContext)

        try {
            val goalsRet = retrofit.getCommunityGoals()

            val goals = try {
                val goals: MutableList<CommunityGoal> = ArrayList()
                for (item in goalsRet) {
                    goals.add(CommunityGoal.fromCommunityGoalsItemResponse(item))
                }
                CommunityGoals(goals)
            } catch (e: Exception) {
                CommunityGoals(ArrayList())
            }
            return ProxyResult(goals, null)
        } catch (t: Throwable) {
            return ProxyResult(null, t)
        }
    }
}