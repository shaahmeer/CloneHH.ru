package com.example.domain.domain.Repository

import com.example.core.core.data.Offer
import com.example.core.core.data.Vacancy

interface Repository {
    suspend fun getOffer(): List<Offer>
    suspend fun getVacancies(): List<Vacancy>
}



