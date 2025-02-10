package com.example.jobtest.presentation.viewmodel

import android.graphics.Color
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
    private val offer: GetOfferUsecase,
    private val vacancies: GetVacanciesUseCase
) : ViewModel() {

    // State Flows for UI
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

    // Debounce for search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        observeSearchQuery() // Start observing search query changes
    }

    // Load Offers
    fun loadOffers() {
        _isLoading.value = true
        viewModelScope.launch {
            runCatching { offer() }
                .onSuccess { _offers.value = it }
                .onFailure { println("Error loading offers: ${it.message}") }
            _isLoading.value = false
        }
    }

    // Load Vacancies
    fun loadVacancies() {
        _isLoading.value = true
        viewModelScope.launch {
            runCatching { vacancies() }
                .onSuccess { data ->
                    _allJobs.value = data
                    _filteredJobs.value = data
                    updateFavoriteVacancies()
                }
                .onFailure { println("Error loading vacancies: ${it.message}") }
            _isLoading.value = false
        }
    }

    // Observe Search Query with Debounce
    private fun observeSearchQuery() {
        _searchQuery
            .debounce(300) // 300ms debounce
            .onEach { query ->
                _filteredJobs.value = if (query.isEmpty()) {
                    _allJobs.value
                } else {
                    _allJobs.value.filter {
                        it.title.contains(query, ignoreCase = true) ||
                                it.company.contains(query, ignoreCase = true)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    // Update Search Query
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Toggle Favorite
    fun toggleFavorite(vacancy: Vacancy) {
        _allJobs.update { currentVacancies ->
            currentVacancies.map { if (it.id == vacancy.id) it.copy(isFavorite = !it.isFavorite) else it }
        }
        _filteredJobs.update { currentFiltered ->
            currentFiltered.map { if (it.id == vacancy.id) it.copy(isFavorite = !it.isFavorite) else it }
        }
        updateFavoriteVacancies()
    }

    // Update Favorite Vacancies
    private fun updateFavoriteVacancies() {
        _favoriteVacancies.value = _allJobs.value.filter { it.isFavorite }
    }

    // Get Favorite Button Color
    fun getFavoriteColor(vacancy: Vacancy): Int {
        return if (vacancy.isFavorite) Color.RED else Color.GRAY
    }
}