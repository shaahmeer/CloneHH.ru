package com.example.jobtest.domain.Adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jobtest.core.data.Vacancy
import com.example.jobtest.databinding.ItemVacancyBinding
import com.example.jobtest.domain.Presentation.Jobdetails
import com.example.jobtest.domain.viewmodel.SharedViewModel

class VacancyAdapter(
    var vacancy: List<Vacancy>,  // Use a mutable list so it can be updated dynamically
    private val onFavoriteClick: (Vacancy) -> Unit,  // Callback for when a favorite is clicked
    private val onItemClick: (Vacancy) -> Unit,  // Callback for item click
) : RecyclerView.Adapter<VacancyAdapter.VacancyViewHolder>() {

    inner class VacancyViewHolder(private val binding: ItemVacancyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(vacancy: Vacancy) {
            binding.apply {
                // Bind Vacancy data to UI components
                tvJobTitle.text = vacancy.title
                tvSalary.text = vacancy.salary.full
                tvLocation.text =
                    "${vacancy.address.town}, ${vacancy.address.street}, ${vacancy.address.house}"
                tvCompanyName.text = vacancy.company
                tvExperience.text = vacancy.experience.previewText
                tvPublishDate.text = vacancy.publishedDate



                btnFavorite.setColorFilter(
                    if (vacancy.isFavorite) android.graphics.Color.RED else android.graphics.Color.GRAY
                )

                binding.btnFavorite.setOnClickListener {
                    onFavoriteClick(vacancy)
                }
                btnApply.setOnClickListener {
                    val intent = Intent(itemView.context, Jobdetails::class.java).apply {
                        putExtra("jobTitle", vacancy.title)
                        putExtra(
                            "location",
                            "${vacancy.address.street}, ${vacancy.address.house}, ${vacancy.address.town}"
                        )
                        putExtra("companyName", vacancy.company)
                        putExtra("experience", vacancy.experience.previewText)
                        putExtra("publishDate", vacancy.publishedDate)
                        putExtra("viewersCount", vacancy.lookingNumber.toString())
                        putExtra("isFavorite", vacancy.isFavorite)
                        putExtra("salary", vacancy.salary.full)
                        putExtra(
                            "schedules",
                            ArrayList(vacancy.schedules)
                        ) // Assuming schedules is a List
                        putExtra("appliedNumber", "человек сейчас смотрят ${vacancy.appliedNumber.toString()} ")
                        putExtra("description", vacancy.description)
                        putExtra("responsibilities", vacancy.responsibilities)
                        putExtra(
                            "questions",
                            ArrayList(vacancy.questions)
                        ) // Assuming questions is a List
                    }
                    itemView.context.startActivity(intent)
                }
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val binding = ItemVacancyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VacancyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(vacancy[position])
    }

    override fun getItemCount(): Int = vacancy.size

    fun updateData(newJobs: List<Vacancy>) {
        vacancy = newJobs.toMutableList()  // Ensure the list is mutable
        notifyDataSetChanged()
    }
}
