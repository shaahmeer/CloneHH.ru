package com.example.domain.domain.usecase

import com.example.domain.domain.Repository.Repository
import com.example.core.core.data.Vacancy
import javax.inject.Inject



    class GetVacanciesUseCase @Inject constructor(
        private val repository: Repository
    ) {
        suspend operator fun invoke(): List<Vacancy> = repository.getVacancies()
    }

