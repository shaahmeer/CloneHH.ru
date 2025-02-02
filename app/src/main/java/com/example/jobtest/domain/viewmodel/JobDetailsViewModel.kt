package com.example.jobtest.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class JobDetailsViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    val jobTitle: LiveData<String> get() = savedStateHandle.getLiveData("jobTitle")
    val location: LiveData<String> get() = savedStateHandle.getLiveData("location")
    val companyName: LiveData<String> get() = savedStateHandle.getLiveData("companyName")
    val experience: LiveData<String> get() = savedStateHandle.getLiveData("experience")
    val publishDate: LiveData<String> get() = savedStateHandle.getLiveData("publishDate")
    val viewersCount: LiveData<String> get() = savedStateHandle.getLiveData("viewersCount")
    val salary: LiveData<String> get() = savedStateHandle.getLiveData("salary")
    val schedules: LiveData<List<String>> get() = savedStateHandle.getLiveData("schedules")
    val appliedNumber: LiveData<Int> get() = savedStateHandle.getLiveData("appliedNumber")
    val description: LiveData<String> get() = savedStateHandle.getLiveData("description")
    val responsibilities: LiveData<List<String>> get() = savedStateHandle.getLiveData("responsibilities")
    val questions: LiveData<List<String>> get() = savedStateHandle.getLiveData("questions")
    val isFavorite: LiveData<Boolean> get() = savedStateHandle.getLiveData("isFavorite")

    // Set job details
    fun setJobDetails(
        jobTitle: String,
        location: String,
        companyName: String,
        experience: String,
        publishDate: String,
        viewersCount: String,
        salary: String,
        schedules: List<String>,
        appliedNumber: Int,
        description: String,
        responsibilities: List<String>,
        questions: List<String>,
        isFavorite: Boolean

    ) {
        savedStateHandle["jobTitle"] = jobTitle
        savedStateHandle["location"] = location
        savedStateHandle["companyName"] = companyName
        savedStateHandle["experience"] = experience
        savedStateHandle["publishDate"] = publishDate
        savedStateHandle["viewersCount"] = viewersCount
        savedStateHandle["salary"] = salary
        savedStateHandle["schedules"] = schedules
        savedStateHandle["appliedNumber"] = appliedNumber
        savedStateHandle["description"] = description
        savedStateHandle["responsibilities"] = responsibilities
        savedStateHandle["questions"] = questions
        savedStateHandle["isFavorite"] = isFavorite
    }

    // Toggle favorite status
    fun toggleFavorite() {
        val currentFavorite = savedStateHandle["isFavorite"] ?: false
        savedStateHandle["isFavorite"] = !currentFavorite
    }
}
