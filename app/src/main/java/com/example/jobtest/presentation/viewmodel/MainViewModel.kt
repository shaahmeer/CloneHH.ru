package com.example.jobtest.presentation.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.jobtest.R
import com.example.domain.domain.Repository.Repository
import com.example.jobtest.presentation.ui.FavoriteVacanciesFragment
import com.example.jobtest.presentation.ui.JobFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: com.example.domain.domain.Repository.Repository
) : ViewModel() {

    private val jobFragment = JobFragment()
    private val favJobFragment = FavoriteVacanciesFragment()

    private val _currentFragment = MutableStateFlow<Fragment?>(jobFragment)
    val currentFragment: StateFlow<Fragment?> = _currentFragment

    private val _favoriteCount = MutableStateFlow(0)
    val favoriteCount: StateFlow<Int> = _favoriteCount

    fun navigateTo(menuItemId: Int) {
        val newFragment = when (menuItemId) {
            R.id.navigation_search -> jobFragment
            R.id.navigation_favorites -> favJobFragment
            else -> null
        }
        newFragment?.let {
            _currentFragment.value = it
        }
    }

    // Method to update the favorite count
    fun updateFavoriteCount(newCount: Int) {
        _favoriteCount.value = newCount
    }
}
