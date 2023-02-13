package com.udacity.asteroidradar.data.api


import com.udacity.asteroidradar.data.models.PictureOfDay
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApiService {
    @GET("neo/rest/v1/feed")
    fun getAsteroidsList(@Query("start_date") startDate: String,
                         @Query("end_date") endDate: String,
                         @Query("api_key") apiKey: String
    ): Deferred<ResponseBody>

    @GET("planetary/apod")
    fun getPictureOfTheDay(@Query("api_key") apiKey: String): Deferred<PictureOfDay>

    companion object {

        val retrofitService: NasaApiService by lazy {
            RetrofitClient.getInstance().create(NasaApiService::class.java)
        }
    }
}