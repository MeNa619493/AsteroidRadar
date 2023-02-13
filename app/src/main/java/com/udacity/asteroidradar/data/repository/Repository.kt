package com.udacity.asteroidradar.data.repository

import com.udacity.asteroidradar.data.api.NasaApiService
import com.udacity.asteroidradar.data.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.data.database.AsteroidDao
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.data.models.Asteroid
import com.udacity.asteroidradar.data.models.PictureOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject

class Repository(private val dao: AsteroidDao, private val apiService: NasaApiService) {

    suspend fun refreshAsteroids(startDate: String, endDate: String) {
        var asteroidList: ArrayList<Asteroid>
        withContext(Dispatchers.IO) {
            val deferredAsteroids: ResponseBody = apiService.getAsteroidsList(
                startDate, endDate, Constants.API_KEY).await()
            asteroidList = parseAsteroidsJsonResult(JSONObject(deferredAsteroids.string()))
            dao.insertAllAsteroids(asteroidList)
        }
    }

    fun getAllAsteroids(): Flow<List<Asteroid>> {
        return dao.getAllAsteroids()
    }

    fun getAsteroidsByCloseApproachDate(startDate: String, endDate: String): Flow<List<Asteroid>> {
        return dao.getAsteroidsByCloseApproachDate(startDate, endDate)
    }

    fun getAsteroidsByTodayDate(startDate: String): Flow<List<Asteroid>> {
        return dao.getAsteroidsByTodayDate(startDate)
    }

    suspend fun getPictureOfTheDay(): PictureOfDay? {
        var pictureOfTheDay: PictureOfDay
        withContext(Dispatchers.IO) {
            pictureOfTheDay = apiService.getPictureOfTheDay(Constants.API_KEY).await()
        }
        if (pictureOfTheDay.mediaType == "image") {
            return pictureOfTheDay
        }
        return null
    }

    suspend fun deleteAsteroidsByDate(startDate: String) {
        withContext(Dispatchers.IO) {
            dao.deleteAsteroidsByDate(startDate)
        }
    }
}