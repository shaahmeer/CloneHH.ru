package com.example.jobtest.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobtest.R
import com.example.jobtest.databinding.FragmentFavoriteVacanciesBinding
import com.example.jobtest.adapter.FavJobAdapter
import com.example.jobtest.presentation.viewmodel.SharedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteVacanciesFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentFavoriteVacanciesBinding? = null
    private val binding get() = _binding!!

    private lateinit var favJobAdapter: FavJobAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteVacanciesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeFavoriteJobs()
    }

    private fun setupRecyclerView() {
        favJobAdapter = FavJobAdapter(
            jobs = emptyList(),
            onFavoriteClick = ::onFavoriteClick,
            onApplyClick = ::onApplyClick,
            onRemoveFavoriteClick = ::onRemoveFavoriteClick
        )

        binding.favRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favJobAdapter
        }
    }

    private fun observeFavoriteJobs() {
        lifecycleScope.launch {
            sharedViewModel.favoriteVacancies.collect { favoriteJobs ->


                favJobAdapter.updateData(favoriteJobs)

                // Handle empty list view
                if (favoriteJobs.isEmpty()) {

                    binding.favRecyclerView.visibility = View.GONE
                } else {

                    binding.favRecyclerView.visibility = View.VISIBLE
                }

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

    private fun onFavoriteClick(position: Int) {
        val job = favJobAdapter.jobs[position]
        sharedViewModel.toggleFavorite(job)
    }

    private fun onApplyClick(position: Int) {
        val job = favJobAdapter.jobs[position]
        Toast.makeText(requireContext(), "Applied for ${job.title} at ${job.company}", Toast.LENGTH_SHORT).show()
    }

    private fun onRemoveFavoriteClick(position: Int) {
        val job = favJobAdapter.jobs[position]
        sharedViewModel.removeFavoriteJob(job)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
