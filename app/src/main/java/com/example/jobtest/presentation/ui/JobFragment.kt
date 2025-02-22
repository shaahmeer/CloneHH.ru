package com.example.jobtest.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.core.data.Vacancy
import com.example.jobtest.databinding.FragmentJobBinding
import com.example.jobtest.adapter.HorizontalAdapter
import com.example.jobtest.adapter.VacancyAdapter
import com.example.jobtest.presentation.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class JobFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentJobBinding? = null
    private val binding get() = _binding!!

    private lateinit var jobAdapter: VacancyAdapter
    private lateinit var offerAdapter: HorizontalAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJobBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapters()
        setupRecyclerViews()
        observeViewModel()

        // Set up SearchView listener
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { sharedViewModel.updateSearchQuery(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                sharedViewModel.updateSearchQuery(newText ?: "")
                return true
            }
        })

        // Load data
        sharedViewModel.loadVacancies()
        sharedViewModel.loadOffers()
    }

    private fun setupAdapters() {
        jobAdapter = VacancyAdapter(
            vacancies = emptyList(),
            viewModel = sharedViewModel,
            onFavoriteClick = ::onFavoriteClick,
            onApplyClick = ::onApplyClick
        )
        offerAdapter = HorizontalAdapter(emptyList())
    }

    private fun setupRecyclerViews() {
        binding.verticalRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = jobAdapter
        }

        binding.horizontalRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = offerAdapter
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            sharedViewModel.filteredJobs.collect { filteredVacancies ->
                jobAdapter.updateData(filteredVacancies)
            }
        }

        lifecycleScope.launch {
            sharedViewModel.offers.collect { offers ->
                offerAdapter.updateData(offers)
            }
        }

        // Observe isLoading state (optional)
        /*lifecycleScope.launch {
            sharedViewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }*/
    }

    private fun onFavoriteClick(vacancy: Vacancy) {
        sharedViewModel.toggleFavorite(vacancy)
    }

    private fun onApplyClick(vacancy: Vacancy) {
        Toast.makeText(requireContext(), "Apply clicked for vacancy: ${vacancy.title}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}