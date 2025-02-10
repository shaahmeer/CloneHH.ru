package com.example.jobtest.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobtest.R
import com.example.jobtest.databinding.FragmentFavoriteVacanciesBinding
import com.example.jobtest.adapter.FavJobAdapter
import com.example.jobtest.presentation.viewmodel.FavoriteVacanciesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class FavoriteVacanciesFragment : Fragment() {

    private val favoriteVacanciesViewModel: FavoriteVacanciesViewModel by activityViewModels()
    private var _binding: FragmentFavoriteVacanciesBinding? = null
    private val binding get() = _binding!!

    private lateinit var favJobAdapter: FavJobAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Use ViewBinding to inflate the layout
        _binding = FragmentFavoriteVacanciesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        setupRecyclerView()

        // Observe favorite jobs from ViewModel
        observeFavoriteJobs()
    }

    private fun setupRecyclerView() {
        // Initialize the adapter with ViewModel
        favJobAdapter = FavJobAdapter(
            jobs = emptyList(),
            viewModel = favoriteVacanciesViewModel
        )

        // Setup RecyclerView with the adapter and layout manager
        binding.favRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favJobAdapter
        }
    }

    private fun observeFavoriteJobs() {
        lifecycleScope.launch {
            // Observe changes in the favorite jobs list
            favoriteVacanciesViewModel.favoriteVacancies.collect { favoriteJobs ->
                // Update the data in the adapter
                favJobAdapter.updateData(favoriteJobs)

                // Handle empty list view
                binding.favRecyclerView.visibility = if (favoriteJobs.isEmpty()) View.GONE else View.VISIBLE

                // Update the favorite count badge in the bottom navigation
                updateFavoritesBadge(favoriteJobs.size)
            }
        }
    }

    private fun updateFavoritesBadge(favoriteCount: Int) {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val menuItem = bottomNavigationView.menu.findItem(R.id.navigation_favorites)

        val badge = bottomNavigationView.getOrCreateBadge(menuItem.itemId)
        badge.isVisible = favoriteCount > 0
        badge.number = favoriteCount
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
