package com.example.jobtest.domain.Adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.jobtest.databinding.HorizontalItemBinding
import com.example.jobtest.core.data.Offer
import com.example.jobtest.core.data.Vacancy

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
            binding.greebutton.text = offer.buttonText ?: "Подробнее"  // Default text if buttonText is null

            binding.link.setOnClickListener {
                if (offer.link.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(offer.link))
                    it.context.startActivity(intent)
                }
            }
        }
    }

    fun updateData(newJobs: List<Offer>) {
        offer = newJobs.toMutableList()
        notifyDataSetChanged()
    }
}
