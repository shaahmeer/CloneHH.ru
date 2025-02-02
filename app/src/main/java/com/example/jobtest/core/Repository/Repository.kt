package com.example.jobtest.core.Repository

import com.example.jobtest.core.data.Offer
import com.example.jobtest.core.data.Vacancy

interface Repository {
    suspend fun getOffer(): List<Offer>
    suspend fun getVacancies(): List<Vacancy>
}



