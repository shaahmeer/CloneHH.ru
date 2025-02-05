package com.example.jobtest.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.core.data.Offer
import com.example.core.core.data.Vacancy
import com.example.domain.domain.usecase.GetOfferUsecase
import com.example.domain.domain.usecase.GetVacanciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class SharedViewModel @Inject constructor(
    private val offer: com.example.domain.domain.usecase.GetOfferUsecase,
    private val vacancies: com.example.domain.domain.usecase.GetVacanciesUseCase
) : ViewModel() {

    private val _allJobs = MutableStateFlow<List<Vacancy>>(emptyList())
    val allJobs: StateFlow<List<Vacancy>> = _allJobs.asStateFlow()

    private val _filteredJobs = MutableStateFlow<List<Vacancy>>(emptyList())
    val filteredJobs: StateFlow<List<Vacancy>> = _filteredJobs.asStateFlow()

    private val _offers = MutableStateFlow<List<Offer>>(emptyList())
    val offers: StateFlow<List<Offer>> = _offers.asStateFlow()

    private val _favoriteVacancies = MutableStateFlow<List<Vacancy>>(emptyList())
    val favoriteVacancies: StateFlow<List<Vacancy>> = _favoriteVacancies.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadOffers() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = runCatching { offer() }
            result.onSuccess { data ->
                _offers.value = data
            }
            result.onFailure { exception ->
                println("Error loading offers: ${exception.message}")
            }
        }
    }

    fun loadVacancies() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = runCatching { vacancies() }
            result.onSuccess { data ->
                _allJobs.value = data
                _filteredJobs.value = data
                updateFavoriteVacancies()
            }
            result.onFailure { exception ->
                println("Error loading vacancies: ${exception.message}")
            }
        }
    }

    fun filterJobs(query: String) {
        val currentJobs = _allJobs.value
        _filteredJobs.value = if (query.isEmpty()) {
            currentJobs
        } else {
            currentJobs.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.company.contains(query, ignoreCase = true)
            }
        }
    }

    fun toggleFavorite(vacancy: Vacancy) {
        _allJobs.update { currentVacancies ->
            currentVacancies.map { if (it.id == vacancy.id) it.copy(isFavorite = !it.isFavorite) else it }
        }
        _filteredJobs.update { currentFiltered ->
            currentFiltered.map { if (it.id == vacancy.id) it.copy(isFavorite = !it.isFavorite) else it }
        }
        updateFavoriteVacancies()
    }

    fun removeFavoriteJob(vacancy: Vacancy) {
        if (vacancy.isFavorite) toggleFavorite(vacancy)
    }

    private fun updateFavoriteVacancies() {
        _favoriteVacancies.value = _allJobs.value.filter { it.isFavorite }
    }
}
