package com.example.jobtest.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobtest.core.data.Vacancy

class JobViewModel : ViewModel() {
    private val _favoriteJobs = MutableLiveData<List<Vacancy>>()
    val favoriteJobs: LiveData<List<Vacancy>> = _favoriteJobs

    // Update favorite job list
    fun updateFavoriteJob(vacancy: Vacancy) {
        val updatedJobs = _favoriteJobs.value?.toMutableList() ?: mutableListOf()
        val jobIndex = updatedJobs.indexOfFirst { it.id == vacancy.id }

        if (jobIndex >= 0) {
            updatedJobs[jobIndex] = vacancy // Update the favorite status
        } else {
            updatedJobs.add(vacancy) // If the job wasn't already in the list, add it
        }

        _favoriteJobs.value = updatedJobs
    }
}
