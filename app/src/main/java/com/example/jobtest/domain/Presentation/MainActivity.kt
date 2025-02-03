package com.example.jobtest.domain.Presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.jobtest.R
import com.example.jobtest.databinding.ActivityMainBinding
import com.example.jobtest.domain.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var jobFragment: JobFragment
    private lateinit var favJobFragment: FavoriteVacanciesFragment
    private var activeFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeFavoritesCount()

        jobFragment = supportFragmentManager.findFragmentByTag("JobFragment") as? JobFragment ?: JobFragment()
        favJobFragment = supportFragmentManager.findFragmentByTag("FavJobFragment") as? FavoriteVacanciesFragment ?: FavoriteVacanciesFragment()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, favJobFragment, "FavJobFragment")
                .hide(favJobFragment)
                .commit()

            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, jobFragment, "JobFragment")
                .commit()

            activeFragment = jobFragment
        } else {
            val savedTag = savedInstanceState.getString("ACTIVE_FRAGMENT")
            activeFragment = if (savedTag == "FavJobFragment") favJobFragment else jobFragment

            supportFragmentManager.beginTransaction()
                .show(activeFragment!!)
                .commit()
        }

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_search -> showFragment(jobFragment)
                R.id.navigation_favorites -> showFragment(favJobFragment)
            }
            true
        }
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
        val badge = binding.bottomNavigation.getOrCreateBadge(R.id.navigation_favorites)
        if (count > 0) {
            badge.isVisible = true
            badge.number = count
        } else {
            badge.isVisible = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("ACTIVE_FRAGMENT", activeFragment?.tag)
    }
}
