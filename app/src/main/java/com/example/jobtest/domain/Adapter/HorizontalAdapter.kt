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

class HorizontalAdapter : ListAdapter<Offer, HorizontalAdapter.OfferViewHolder>(OfferDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val binding = HorizontalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OfferViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val offer = getItem(position)  // Use `getItem()` for ListAdapter
        holder.bind(offer)
    }

    override fun getItemCount(): Int = currentList.size

    // ViewHolder class to bind the offer data
    inner class OfferViewHolder(private val binding: HorizontalItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(offer: Offer) {
            // Bind the title and button text
            binding.Titleview.text = offer.title
            binding.greebutton.text = offer.buttonText ?: "Подробнее"  // Default text if buttonText is null

            // Make the link clickable and open it in a browser
            binding.link.setOnClickListener {
                // Check if the link is not empty or null
                if (offer.link.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(offer.link))
                    it.context.startActivity(intent)
                }
            }
        }
    }

    // DiffUtil callback for optimizing list updates
    class OfferDiffCallback : DiffUtil.ItemCallback<Offer>() {
        override fun areItemsTheSame(oldItem: Offer, newItem: Offer): Boolean {
            return oldItem.id == newItem.id  // Assuming `id` is a unique identifier for the Offer
        }

        override fun areContentsTheSame(oldItem: Offer, newItem: Offer): Boolean {
            return oldItem == newItem
        }
    }
}
