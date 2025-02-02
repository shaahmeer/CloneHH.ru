package com.example.jobtest.domain.Presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jobtest.R



import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.core.content.ContextCompat
import com.example.jobtest.databinding.ActivityJobDetailsBinding
import com.example.jobtest.domain.viewmodel.JobDetailsViewModel
import com.google.android.material.button.MaterialButton

class Jobdetails : AppCompatActivity() {
    private lateinit var binding: ActivityJobDetailsBinding
    private val viewModel: JobDetailsViewModel by viewModels() // Lazy initialization of ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val jobTitle = intent.getStringExtra("jobTitle") ?: ""
        val location = intent.getStringExtra("location") ?: ""
        val companyName = intent.getStringExtra("companyName") ?: ""
        val experience = intent.getStringExtra("experience") ?: ""
        val publishDate = intent.getStringExtra("publishDate") ?: ""
        val viewersCount = intent.getStringExtra("viewersCount") ?: ""
        val isFavorite = intent.getBooleanExtra("isFavorite", false)
        val salary = intent.getStringExtra("salary") ?: "Уровень дохода не указан"
        val schedules = intent.getStringArrayListExtra("schedules") ?: arrayListOf()
        val appliedNumber = intent.getIntExtra("appliedNumber", -1)
        val description = intent.getStringExtra("description") ?: ""
        val responsibilities = intent.getStringExtra("responsibilities")?.split("\n") ?: listOf()
        val questions = intent.getStringArrayListExtra("questions") ?: arrayListOf()

        // Load data into ViewModel
        viewModel.setJobDetails(
            jobTitle,
            location,
            companyName,
            experience,
            publishDate,
            viewersCount,
            salary,
            schedules,
            appliedNumber,
            description,
            responsibilities,
            questions,
            isFavorite
        )

        // Observe ViewModel LiveData and update UI
        viewModel.jobTitle.observe(this, Observer { binding.tvJobTitle.text = it })
        viewModel.location.observe(this, Observer { binding.tvLocation.text = it })
        viewModel.companyName.observe(this, Observer { binding.tvCompanyDescription.text = it })
        viewModel.experience.observe(this, Observer { binding.tvExperience.text = it })
        viewModel.viewersCount.observe(this, Observer { binding.tvViewersCount.text = it })
        viewModel.salary.observe(this, Observer { binding.tvIncome.text = it })
        viewModel.schedules.observe(this, Observer {
            binding.tvEmployment.text = it.joinToString(", ")
        })
        viewModel.appliedNumber.observe(this, Observer { appliedNumber ->
            binding.tvApplicantsCount.text = if (appliedNumber >= 0) {
                "$appliedNumber человек(а) откликнулось"
            } else {
                "Количество откликов не указано"
            }
        })
        viewModel.description.observe(this, Observer { binding.tvCompanyDescription.text = it })
        viewModel.responsibilities.observe(this, Observer { responsibilities ->
            binding.layoutResponsibilities.removeAllViews()
            responsibilities.forEach { task ->
                val taskTextView = TextView(this).apply {
                    text = "• $task"
                    setTextColor(ContextCompat.getColor(this@Jobdetails, R.color.white))
                    textSize = 16f
                }
                binding.layoutResponsibilities.addView(taskTextView)
            }
        })
        viewModel.questions.observe(this, Observer { questions ->
            binding.tvQuestions.text = "Часто задаваемые вопросы"
            questions.forEach { question ->
                val questionButton = MaterialButton(this).apply {
                    text = question
                    setTextColor(ContextCompat.getColor(this@Jobdetails, R.color.white))
                    backgroundTintList =
                        ContextCompat.getColorStateList(this@Jobdetails, R.color.black)
                }
                binding.layoutResponsibilities.addView(questionButton) // Add questions to UI
            }
        })

        // Observe favorite status
        viewModel.isFavorite.observe(this, Observer { isFavorite ->
            binding.btnFavorite.setColorFilter(
                if (isFavorite) android.graphics.Color.RED else android.graphics.Color.GRAY
            )
        })

        // Set up click listeners
        binding.btnBack.setOnClickListener { finish() }
        binding.btnFavorite.setOnClickListener { viewModel.toggleFavorite() }
        binding.btnApply.setOnClickListener {
            // Handle application logic here
        }
    }
}
