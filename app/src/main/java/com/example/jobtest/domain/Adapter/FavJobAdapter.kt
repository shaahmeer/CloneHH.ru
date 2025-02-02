package com.example.jobtest.domain.Adapter


import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jobtest.core.data.Vacancy
import com.example.jobtest.databinding.ItemVacancyBinding
import com.example.jobtest.domain.Presentation.Jobdetails


class FavJobAdapter(
    var jobs: List<Vacancy>,
    private val onFavoriteClick: (Int) -> Unit,
    private val onApplyClick: (Int) -> Unit,
    private val onRemoveFavoriteClick: (Int) -> Unit
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

                binding.btnFavorite.setColorFilter(
                    if (vacancy.isFavorite) android.graphics.Color.RED else android.graphics.Color.GRAY
                )

            binding.btnFavorite.setOnClickListener {
                onRemoveFavoriteClick(adapterPosition) // This is for removing from favorites
            }
                btnApply.setOnClickListener {
                    val intent = Intent(root.context, Jobdetails::class.java).apply {
                        putExtra("jobTitle", vacancy.title)
                        putExtra("location", "${vacancy.address.street}, ${vacancy.address.house}, ${vacancy.address.town}")
                        putExtra("companyName", vacancy.company)
                        putExtra("experience", vacancy.experience.previewText)
                        putExtra("publishDate", vacancy.publishedDate)
                        putExtra("viewersCount", vacancy.lookingNumber)
                        putExtra("isFavorite", vacancy.isFavorite)
                        putExtra("salary", vacancy.salary.full)
                        putExtra("schedules", ArrayList(vacancy.schedules))
                        putExtra("appliedNumber", " ${vacancy.appliedNumber.toString()} ")
                        putExtra("description", vacancy.description)
                        putExtra("responsibilities", vacancy.responsibilities)
                        putExtra("questions", ArrayList(vacancy.questions))
                    }
                    root.context.startActivity(intent)
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
