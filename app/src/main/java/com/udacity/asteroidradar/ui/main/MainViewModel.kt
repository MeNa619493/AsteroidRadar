package com.udacity.asteroidradar.ui.main

import androidx.lifecycle.*
import com.udacity.asteroidradar.data.models.Asteroid
import com.udacity.asteroidradar.data.models.PictureOfDay
import com.udacity.asteroidradar.data.repository.Repository
import com.udacity.asteroidradar.utils.getEndDate
import com.udacity.asteroidradar.utils.getStartDate
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

enum class NasaApiStatus { LOADING, ERROR, DONE }

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _navigateToDetailFragmentEvent = MutableLiveData<Asteroid>()
    val navigateToDetailFragmentEvent: LiveData<Asteroid>
        get() = _navigateToDetailFragmentEvent

    private var _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfTheDay: LiveData<PictureOfDay>
        get() = _pictureOfTheDay


    private val _status = MutableLiveData<NasaApiStatus>()
    val status: LiveData<NasaApiStatus>
        get() = _status

    init {
        onTodayAsteroidsMenuItemSelected()
        viewModelScope.launch {
            _status.value = NasaApiStatus.LOADING
            try {
                repository.refreshAsteroids(getStartDate(), getEndDate())
                _status.value = NasaApiStatus.DONE
                getPictureOfTheDay()
            } catch (e: Exception) {
                _status.value = NasaApiStatus.ERROR
                println("Exception getting data from nasa api: $e.message")
            }
        }
    }

    fun openDetailFragment(asteroid: Asteroid) {
        _navigateToDetailFragmentEvent.value = asteroid
    }

    fun doneNavigating() {
        _navigateToDetailFragmentEvent.value = null
    }

    fun onViewWeekAsteroidsMenuItemSelected() {
        viewModelScope.launch {
            repository.getAsteroidsByCloseApproachDate(getStartDate(), getEndDate()).collect {
                    asteroids -> _asteroidList.value = asteroids
            }
        }
    }

    fun onTodayAsteroidsMenuItemSelected() {
        viewModelScope.launch {
            repository.getAsteroidsByTodayDate(getStartDate()).collect {
                    asteroids -> _asteroidList.value = asteroids
            }
        }
    }

    fun onSavedAsteroidsMenuItemSelected() {
        viewModelScope.launch {
            repository.getAllAsteroids().collect {
                    asteroids -> _asteroidList.value = asteroids
            }
        }
    }

    private suspend fun getPictureOfTheDay() {
        if (repository.getPictureOfTheDay() != null){
            _pictureOfTheDay.value = repository.getPictureOfTheDay()
        } else {
            println("picture of the day is video")
        }
    }
}