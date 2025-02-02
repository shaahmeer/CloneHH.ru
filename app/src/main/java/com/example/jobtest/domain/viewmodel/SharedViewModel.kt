package com.example.jobtest.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobtest.core.Repository.Repository
import com.example.jobtest.core.data.Offer
import com.example.jobtest.core.data.Vacancy
import kotlinx.coroutines.launch

class SharedViewModel(
    private val repository: Repository,
    private val mainViewModel: MainViewModel
) : ViewModel() {

    private val _allJobs = MutableLiveData<List<Vacancy>>()
    val allJobs: LiveData<List<Vacancy>> get() = _allJobs

    private val _filteredJobs = MutableLiveData<List<Vacancy>>()
    val filteredJobs: LiveData<List<Vacancy>> get() = _filteredJobs
    private val _offers = MutableLiveData<List<Offer>>()
    val offers: LiveData<List<Offer>> get() = _offers

    private val _favoriteVacancies = MutableLiveData<List<Vacancy>>()
    val favoriteVacancies: LiveData<List<Vacancy>> get() = _favoriteVacancies

    fun loadOffers() {
        viewModelScope.launch {
            try {
                val offers = repository.getOffer()  // Make sure your repository has a method for this
                _offers.value = offers
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    fun loadVacancies() {
        viewModelScope.launch {
            try {
                val vacancies = repository.getVacancies()
                _allJobs.value = vacancies
                _filteredJobs.value = vacancies
                updateFavoriteVacancies()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    // Filter jobs based on query
    fun filterJobs(query: String) {
        val currentJobs = _allJobs.value ?: return
        _filteredJobs.value = if (query.isEmpty()) {
            currentJobs
        } else {
            currentJobs.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.company.contains(query, ignoreCase = true)
            }
        }
    }

    // Toggle the favorite status of a vacancy
    fun toggleFavorite(vacancy: Vacancy) {
        val currentVacancies = _allJobs.value?.toMutableList() ?: return
        val index = currentVacancies.indexOf(vacancy)
        if (index != -1) {
            val updatedVacancy = vacancy.copy(isFavorite = !vacancy.isFavorite)
            currentVacancies[index] = updatedVacancy
            _allJobs.value = currentVacancies

            // Update filtered jobs as well
            _filteredJobs.value = _filteredJobs.value?.map {
                if (it == vacancy) updatedVacancy else it
            }

            // Update the favorite vacancies
            updateFavoriteVacancies()
        }
    }

    fun removeFavoriteJob(vacancy: Vacancy) {
        if (vacancy.isFavorite) toggleFavorite(vacancy)
    }

    private fun updateFavoriteVacancies() {
        _favoriteVacancies.value = _allJobs.value?.filter { it.isFavorite }
        // Update the favorite count in MainViewModel
        _favoriteVacancies.value?.let {
            mainViewModel.updateFavoriteCount(it.size)
        }
    }
}
