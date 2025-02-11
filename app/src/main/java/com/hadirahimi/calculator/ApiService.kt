package com.hadirahimi.calculator

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

data class Coins(
    val result: String,
    val documentation: String,
    val terms_of_use: String,
    val time_last_update_unix: Long,
    val time_last_update_utc: String,
    val time_next_update_unix: Long,
    val time_next_update_utc: String,
    val base_code: String,
    val conversion_rates: Map<String, Double>
)

interface ApiService {
    @GET("{key}/latest/{coin}")
    fun get(@Path("key") key: String, @Path("coin") coin: String): Call<Coins>
}
