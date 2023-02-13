package com.udacity.asteroidradar.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.data.models.Asteroid
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllAsteroids(asteroids: List<Asteroid>)

    @Query("SELECT * FROM asteroid ORDER BY closeApproachDate ASC")
    fun getAllAsteroids(): Flow<List<Asteroid>>

    @Query("SELECT * FROM Asteroid WHERE closeApproachDate = :startDate AND closeApproachDate <= :endDate ORDER BY closeApproachDate ASC")
    fun getAsteroidsByCloseApproachDate(startDate: String, endDate: String): Flow<List<Asteroid>>

    @Query("SELECT * FROM Asteroid WHERE closeApproachDate = :startDate ORDER BY closeApproachDate ASC")
    fun getAsteroidsByTodayDate(startDate: String): Flow<List<Asteroid>>

    @Query("DELETE FROM Asteroid WHERE closeApproachDate != :today")
    fun deleteAsteroidsByDate(today: String)
}