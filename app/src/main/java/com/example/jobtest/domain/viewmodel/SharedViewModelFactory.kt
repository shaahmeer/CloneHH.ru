package com.example.jobtest.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jobtest.core.Repository.Repository

class SharedViewModelFactory(
    private val repository: Repository,
    private val mainViewModel: MainViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            return SharedViewModel(repository, mainViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
