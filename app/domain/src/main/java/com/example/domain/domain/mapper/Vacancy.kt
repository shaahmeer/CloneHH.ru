package com.example.domain.domain.mapper

import com.example.core.core.data.Vacancy
import com.example.core.core.data.Address
import com.example.core.core.data.Experience
import com.example.core.core.data.Salary

fun Vacancy.toDomainVacancy(): Vacancy {
    return Vacancy(
        id = this.id,
        lookingNumber = this.lookingNumber,
        viewersCount = this.viewersCount,
        title = this.title,
        address = Address(
            town = this.address.town,
            street = this.address.street,
            house = this.address.house
        ),
        company = this.company,
        experience = Experience(
            previewText = this.experience.previewText,
            text = this.experience.text
        ),
        publishedDate = this.publishedDate,
        isFavorite = this.isFavorite,
        salary = Salary(full = this.salary.full),
        schedules = this.schedules,
        appliedNumber = this.appliedNumber,
        description = this.description,
        responsibilities = this.responsibilities,
        questions = this.questions
    )
}
