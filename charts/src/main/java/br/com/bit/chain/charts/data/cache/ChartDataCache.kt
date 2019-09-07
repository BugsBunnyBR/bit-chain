package br.com.bit.chain.charts.data.cache

import android.content.SharedPreferences
import br.com.bit.chain.charts.data.models.ChartDataResponse
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject

internal interface ChartDataLocalCache {
    fun get(): Maybe<ChartDataResponse>
    fun save(chartDataResponse: ChartDataResponse): Completable
}

// TODO: Decouple models and Gson from network and cache.
internal class ChartDataLocalCacheImpl @Inject constructor(
    private val prefs: SharedPreferences,
    private val gson: Gson
) : ChartDataLocalCache {
    private val key: String = "cache key"

    override fun get(): Maybe<ChartDataResponse> {
        return Maybe.create { emitter ->
            val json = prefs.getString(key, null)
            if (json == null) {
                emitter.onComplete()
            } else {
                val data = gson.fromJson(json, ChartDataResponse::class.java)
                emitter.onSuccess(data)
            }
        }
    }

    override fun save(chartDataResponse: ChartDataResponse): Completable {
        return Completable.fromAction {
            val json = gson.toJson(chartDataResponse)
            prefs.edit()
                .putString(key, json)
                .apply()
        }
    }
}