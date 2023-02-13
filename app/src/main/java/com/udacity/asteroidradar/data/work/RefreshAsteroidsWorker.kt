package com.udacity.asteroidradar.data.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.data.repository.Repository
import com.udacity.asteroidradar.utils.getEndDate
import com.udacity.asteroidradar.utils.getStartDate
import retrofit2.HttpException

class RefreshAsteroidsWorker(
    appContext: Context,
    params: WorkerParameters,
    private val repository: Repository
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = try {
        repository.refreshAsteroids(getStartDate(), getEndDate())
        Result.success()
    } catch (exception: HttpException) {
        Result.retry()
    }
}