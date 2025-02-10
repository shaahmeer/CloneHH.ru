package com.example.jobtest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.core.core.data.Vacancy
import com.example.jobtest.databinding.ItemVacancyBinding
import com.example.jobtest.presentation.viewmodel.SharedViewModel


class VacancyAdapter(
    private var vacancies: List<Vacancy>,
    private val viewModel: SharedViewModel, // Pass ViewModel to Adapter
    private val onFavoriteClick: (Vacancy) -> Unit,
    private val onApplyClick: (Vacancy) -> Unit
) : RecyclerView.Adapter<VacancyAdapter.VacancyViewHolder>() {

    inner class VacancyViewHolder(private val binding: ItemVacancyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(vacancy: Vacancy) {
            binding.apply {
                tvJobTitle.text = vacancy.title
                tvSalary.text = vacancy.salary.full
                tvLocation.text = "${vacancy.address.town}, ${vacancy.address.street}, ${vacancy.address.house}"
                tvCompanyName.text = vacancy.company
                tvExperience.text = vacancy.experience.previewText
                tvPublishDate.text = vacancy.publishedDate

                btnFavorite.setColorFilter(viewModel.getFavoriteColor(vacancy))

                btnFavorite.setOnClickListener { onFavoriteClick(vacancy) }
                btnApply.setOnClickListener { onApplyClick(vacancy) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val binding = ItemVacancyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VacancyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(vacancies[position])
    }

    override fun getItemCount(): Int = vacancies.size

    fun updateData(newVacancies: List<Vacancy>) {
        vacancies = newVacancies
        notifyDataSetChanged()
    }
}
