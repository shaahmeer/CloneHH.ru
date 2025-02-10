package com.example.jobtest.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jobtest.databinding.HorizontalItemBinding
import com.example.core.core.data.Offer

class HorizontalAdapter(var offer: List<Offer>,) : RecyclerView.Adapter<HorizontalAdapter.OfferViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val binding = HorizontalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OfferViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        holder.bind(offer[position])
    }

    override fun getItemCount(): Int = offer.size

    inner class OfferViewHolder(private val binding: HorizontalItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(offer: Offer) {
            binding.Titleview.text = offer.title
            binding.greebutton.text = offer.buttonText ?: "Подробнее"


        }
    }

    fun updateData(newJobs: List<Offer>) {
        offer = newJobs.toMutableList()
        notifyDataSetChanged()
    }
}
