package com.example.jobtest.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.jobtest.R
import com.example.jobtest.databinding.ActivityJobDetailsBinding
import com.example.jobtest.presentation.viewmodel.JobDetailsViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobDetailsFragment : Fragment() {
    private var _binding: ActivityJobDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: JobDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = ActivityJobDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        binding.btnBack.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        binding.btnFavorite.setOnClickListener { viewModel.toggleFavorite() }
        binding.btnApply.setOnClickListener { viewModel.applyJob() }
    }

    private fun observeViewModel() {
        viewModel.jobDetails.observe(viewLifecycleOwner) { job ->
            binding.tvJobTitle.text = job.jobTitle
            binding.tvLocation.text = job.location
            binding.tvCompanyDescription.text = job.companyName
            binding.tvExperience.text = job.experience
            binding.tvViewersCount.text = job.viewersCount
            binding.tvIncome.text = job.salary
            binding.tvEmployment.text = job.schedules.joinToString(", ")
            binding.tvApplicantsCount.text =
                if (job.appliedNumber >= 0) "${job.appliedNumber} человек(а) откликнулось"
                else "Количество откликов не указано"
            binding.tvCompanyDescription.text = job.description

            binding.layoutResponsibilities.removeAllViews()
            job.responsibilities.forEach { task ->
                val taskTextView = TextView(requireContext()).apply {
                    text = "• $task"
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    textSize = 16f
                }
                binding.layoutResponsibilities.addView(taskTextView)
            }

            binding.layoutResponsibilities.removeAllViews()
            binding.tvQuestions.text = "Часто задаваемые вопросы"
            job.questions.forEach { question ->
                val questionButton = MaterialButton(requireContext()).apply {
                    text = question
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.black)
                }
                binding.layoutResponsibilities.addView(questionButton)
            }
        }

        viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            binding.btnFavorite.setColorFilter(
                if (isFavorite) android.graphics.Color.RED else android.graphics.Color.GRAY
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
