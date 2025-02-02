package com.example.jobtest.domain.Presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobtest.core.Repository.Repository
import com.example.jobtest.core.Repository.RepositoryImpl
import com.example.jobtest.core.data.Vacancy
import com.example.jobtest.core.datasource.FileDownloadService
import com.example.jobtest.core.datasource.RemoteDatasource
import com.example.jobtest.core.network.RetrofitInstance
import com.example.jobtest.databinding.FragmentJobBinding
import com.example.jobtest.domain.Adapter.HorizontalAdapter
import com.example.jobtest.domain.Adapter.VacancyAdapter
import com.example.jobtest.domain.viewmodel.SharedViewModelFactory
import com.example.jobtest.domain.viewmodel.SharedViewModel
import com.example.jobtest.domain.viewmodel.MainViewModel

class JobFragment : Fragment() {
    private var _binding: FragmentJobBinding? = null
    private val binding get() = _binding!!

    private lateinit var jobAdapter: VacancyAdapter
    private lateinit var offerAdapter: HorizontalAdapter

    // Initialize repository before passing it to SharedViewModelFactory
    private val repository = RepositoryImpl(
        RemoteDatasource(RetrofitInstance.fileDownloadService) // Pass the Retrofit service
    )

    // Get the MainViewModel
    private val mainViewModel: MainViewModel by activityViewModels()

    // Initialize SharedViewModel using the ViewModelFactory
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory(repository, mainViewModel) // Ensure the factory is used correctly
    }

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
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { sharedViewModel.filterJobs(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                sharedViewModel.filterJobs(newText ?: "")
                return true
            }
        })

        // Load jobs from API if the list is empty
        if (sharedViewModel.allJobs.value.isNullOrEmpty()) {
            sharedViewModel.loadVacancies()
        }
        if (sharedViewModel.offers.value.isNullOrEmpty()) {
            sharedViewModel.loadOffers()  // Fetch offers
        }
    }

    private fun setupAdapters() {
        jobAdapter = VacancyAdapter(emptyList(), ::onFavoriteClick, ::onApplyClick)
        offerAdapter = HorizontalAdapter()
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
        sharedViewModel.allJobs.observe(viewLifecycleOwner) { vacancies ->
            jobAdapter.updateData(vacancies) // Update UI with API data
        }

        sharedViewModel.filteredJobs.observe(viewLifecycleOwner) { filteredVacancies ->
            jobAdapter.updateData(filteredVacancies) // Update UI with filtered vacancies

        }

        sharedViewModel.offers.observe(viewLifecycleOwner) { offers ->
            offerAdapter.submitList(offers)  // Update the offer adapter with fetched offers
        }


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
