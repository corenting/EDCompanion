package fr.corenting.edcompanion.network

import android.content.Context
import fr.corenting.edcompanion.singletons.RetrofitSingleton
import java.util.*

object AutoCompleteNetwork {

    private const val MAX_RESULTS = 10

    fun searchSystems(context: Context, filter: String): List<String> {
        return try {
            val edApiV4Retrofit =
                RetrofitSingleton.getInstance().getEdApiV4Retrofit(context.applicationContext)

            val response = edApiV4Retrofit.getSystemsTypeAhead(filter).execute()
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                emptyList()
            } else {
                body.take(MAX_RESULTS)
            }

        } catch (e: Exception) {
            emptyList()
        }
    }

    fun searchShips(context: Context, filter: String): List<String> {
        return try {
            val edApiV4Retrofit = RetrofitSingleton.getInstance()
                .getEdApiV4Retrofit(context.applicationContext)

            val response = edApiV4Retrofit.getShipsTypeAhead(filter).execute()
            val ships = response.body()

            if (!response.isSuccessful || ships == null) {
                emptyList()
            } else {
                ships.take(MAX_RESULTS)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun searchCommodities(context: Context, filter: String): List<String> {
        return try {
            val edApiV4Retrofit = RetrofitSingleton.getInstance()
                .getEdApiV4Retrofit(context.applicationContext)

            val response = edApiV4Retrofit.getCommoditiesTypeAhead(filter).execute()
            val commodities = response.body()

            if (!response.isSuccessful || commodities == null) {
                ArrayList()
            } else {

                commodities.take(MAX_RESULTS)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}