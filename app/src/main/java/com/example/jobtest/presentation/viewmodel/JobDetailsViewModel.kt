package com.example.jobtest.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class JobDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _jobDetails = MutableLiveData<JobDetails>()
    val jobDetails: LiveData<JobDetails> get() = _jobDetails

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    init {
        loadJobDetails()
    }

    private fun loadJobDetails() {
        val jobDetails = JobDetails(
            jobTitle = savedStateHandle["jobTitle"] ?: "",
            location = savedStateHandle["location"] ?: "",
            companyName = savedStateHandle["companyName"] ?: "",
            experience = savedStateHandle["experience"] ?: "",
            publishDate = savedStateHandle["publishDate"] ?: "",
            viewersCount = savedStateHandle["viewersCount"] ?: "",
            salary = savedStateHandle["salary"] ?: "Уровень дохода не указан",
            schedules = savedStateHandle["schedules"] ?: emptyList(),
            appliedNumber = savedStateHandle["appliedNumber"] ?: -1,
            description = savedStateHandle["description"] ?: "",
            responsibilities = savedStateHandle["responsibilities"] ?: emptyList(),
            questions = savedStateHandle["questions"] ?: emptyList(),
        )
        _jobDetails.value = jobDetails
        _isFavorite.value = savedStateHandle["isFavorite"] ?: false
    }

    fun toggleFavorite() {
        _isFavorite.value = _isFavorite.value?.not()
        savedStateHandle["isFavorite"] = _isFavorite.value
    }

    fun applyJob() {
    }
}

data class JobDetails(
    val jobTitle: String,
    val location: String,
    val companyName: String,
    val experience: String,
    val publishDate: String,
    val viewersCount: String,
    val salary: String,
    val schedules: List<String>,
    val appliedNumber: Int,
    val description: String,
    val responsibilities: List<String>,
    val questions: List<String>
)
