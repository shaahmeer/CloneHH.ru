package com.example.jobtest.domain.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobtest.R
import com.example.jobtest.core.Repository.Repository
import com.example.jobtest.domain.Presentation.FavoriteVacanciesFragment
import com.example.jobtest.domain.Presentation.JobFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    // ViewModel code


    private val jobFragment = JobFragment() // Keep a single instance
    private val favJobFragment = FavoriteVacanciesFragment() // Keep a single instance

    private val _currentFragment = MutableLiveData<Fragment>()
    val currentFragment: LiveData<Fragment> get() = _currentFragment

    init {
        _currentFragment.value = jobFragment // Default fragment
    }

    fun navigateTo(menuItemId: Int) {
        val newFragment = when (menuItemId) {
            R.id.navigation_search -> jobFragment
            R.id.navigation_favorites -> favJobFragment
            else -> null
        }
        newFragment?.let { _currentFragment.value = it }
    }

    // Business Logic for Favorite Count
    private val _favoriteCount = MutableStateFlow(0) // Default is 0
    val favoriteCount: StateFlow<Int> = _favoriteCount

    // Method to update favorite count
    fun updateFavoriteCount(newCount: Int) {
        _favoriteCount.value = newCount
    }
}
