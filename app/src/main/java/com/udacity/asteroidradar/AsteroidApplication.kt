package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.data.api.NasaApiService
import com.udacity.asteroidradar.data.database.AsteroidDatabase
import com.udacity.asteroidradar.data.repository.Repository
import com.udacity.asteroidradar.data.work.DeleteAsteroidsWorker
import com.udacity.asteroidradar.data.work.RefreshAsteroidsWorker
import com.udacity.asteroidradar.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidApplication: Application() {

    private val apiService by lazy { NasaApiService.retrofitService }
    private val database by lazy { AsteroidDatabase.getInstance(this) }

    val repository by lazy { Repository(database.asteroidDao, apiService) }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.Default).launch {
            setupDailyWorkers()
        }
    }

    private fun setupDailyWorkers() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .setRequiresStorageNotLow(true)
            .build()

        val refreshRequest = PeriodicWorkRequestBuilder<RefreshAsteroidsWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            Constants.REFRESH_ASTEROIDS_WORKER_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            refreshRequest
        )

        val deleteRequest = PeriodicWorkRequestBuilder<DeleteAsteroidsWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            Constants.DELETE_ASTEROIDS_WORKER_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            deleteRequest
        )
    }
}