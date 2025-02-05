package com.example.domain.domain.usecase

import com.example.domain.domain.Repository.Repository
import com.example.core.core.data.Offer

import javax.inject.Inject

class GetOfferUsecase  @Inject constructor(
        private val repository: Repository
)
     {
        suspend operator fun invoke(): List<Offer> = repository.getOffer()
    }
