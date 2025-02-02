package com.example.jobtest.domain.Presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.jobtest.R
import com.example.jobtest.core.Repository.Repository
import com.example.jobtest.core.Repository.RepositoryImpl
import com.example.jobtest.core.datasource.RemoteDatasource
import com.example.jobtest.core.network.RetrofitInstance
import com.example.jobtest.domain.viewmodel.MainViewModel
import com.example.jobtest.domain.viewmodel.SharedViewModel
import com.example.jobtest.domain.viewmodel.SharedViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    // Declare SharedViewModel and Repository variables
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var repository: Repository

    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var jobFragment: JobFragment
    private lateinit var favJobFragment: FavoriteVacanciesFragment
    private var activeFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = RepositoryImpl(RemoteDatasource(RetrofitInstance.fileDownloadService))

        sharedViewModel = ViewModelProvider(this, SharedViewModelFactory(repository, mainViewModel)).get(SharedViewModel::class.java)


        jobFragment = JobFragment()
        favJobFragment = FavoriteVacanciesFragment()
        activeFragment = jobFragment

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, favJobFragment, "FavJobFragment")
            .hide(favJobFragment)
            .commit()

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, jobFragment, "JobFragment")
            .commit()

        // Set up Bottom Navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_search -> showFragment(jobFragment)
                R.id.navigation_favorites -> showFragment(favJobFragment)
            }
            true
        }

        observeFavoritesCount()
    }

    private fun showFragment(fragment: Fragment) {
        if (fragment != activeFragment) {
            supportFragmentManager.beginTransaction()
                .hide(activeFragment!!)
                .show(fragment)
                .commit()
            activeFragment = fragment
        }
    }

    private fun observeFavoritesCount() {
        lifecycleScope.launch {
            mainViewModel.favoriteCount.collect { count ->
                updateFavoritesBadge(count)
            }
        }
    }

    private fun updateFavoritesBadge(count: Int) {
        val badge = bottomNavigationView.getOrCreateBadge(R.id.navigation_favorites)
        if (count > 0) {
            badge.isVisible = true
            badge.number = count
        } else {
            badge.isVisible = false
        }
    }
}
