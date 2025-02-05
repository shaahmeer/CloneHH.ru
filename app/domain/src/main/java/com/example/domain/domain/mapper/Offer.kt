package com.example.domain.domain.mapper

import com.example.core.core.data.Offer

fun Offer.toDomainOffer():Offer {
    return Offer(
        id = this.id,
        title = this.title,
        link = this.link,
        buttonText = this.buttonText

    )
}
