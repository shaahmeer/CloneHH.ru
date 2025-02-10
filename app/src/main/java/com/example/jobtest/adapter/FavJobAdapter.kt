package com.example.jobtest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.core.core.data.Vacancy
import com.example.jobtest.databinding.ItemVacancyBinding
import com.example.jobtest.presentation.viewmodel.FavoriteVacanciesViewModel

class FavJobAdapter(
    private var jobs: List<Vacancy>,
    private val viewModel: FavoriteVacanciesViewModel
) : RecyclerView.Adapter<FavJobAdapter.JobViewHolder>() {

    inner class JobViewHolder(private val binding: ItemVacancyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(vacancy: Vacancy) {
            binding.apply {
                tvJobTitle.text = vacancy.title
                tvSalary.text = vacancy.salary.full
                tvLocation.text = "${vacancy.address.town}, ${vacancy.address.street}, ${vacancy.address.house}"
                tvCompanyName.text = vacancy.company
                tvExperience.text = vacancy.experience.previewText
                tvPublishDate.text = vacancy.publishedDate

                // Call ViewModel to determine the color of the favorite button
                viewModel.getFavoriteButtonColor(vacancy) { color ->
                    binding.btnFavorite.setColorFilter(color)
                }

                // Set onClick listeners to notify the ViewModel of user actions
                btnFavorite.setOnClickListener {
                    viewModel.toggleFavorite(vacancy)
                }

                btnApply.setOnClickListener {
                    viewModel.onApplyClick(binding.root.context, vacancy)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = ItemVacancyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.bind(jobs[position])
    }

    override fun getItemCount(): Int = jobs.size

    fun updateData(newJobs: List<Vacancy>) {
        jobs = newJobs
        notifyDataSetChanged()
    }
}
