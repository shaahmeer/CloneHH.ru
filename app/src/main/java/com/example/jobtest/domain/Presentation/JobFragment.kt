package com.example.jobtest.domain.Presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobtest.core.Repository.Repository
import com.example.jobtest.core.data.Vacancy
import com.example.jobtest.databinding.FragmentJobBinding
import com.example.jobtest.domain.Adapter.HorizontalAdapter
import com.example.jobtest.domain.Adapter.VacancyAdapter
import com.example.jobtest.domain.viewmodel.MainViewModel
import com.example.jobtest.domain.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint

class JobFragment : Fragment() {

    @Inject
    lateinit var repository: Repository

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

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

        sharedViewModel.loadVacancies()
        sharedViewModel.loadOffers()
    }

    private fun setupAdapters() {
        jobAdapter = VacancyAdapter(emptyList(), ::onFavoriteClick, ::onApplyClick)
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
        sharedViewModel.filteredJobs.observe(viewLifecycleOwner) { filteredVacancies ->
            jobAdapter.updateData(filteredVacancies)
        }

        sharedViewModel.offers.observe(viewLifecycleOwner) { offers ->
            offerAdapter.updateData(offers)
        }

//        sharedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
//            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//        }
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
