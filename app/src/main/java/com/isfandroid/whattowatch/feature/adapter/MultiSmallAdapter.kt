package com.isfandroid.whattowatch.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.isfandroid.core.databinding.ItemMultiSmallBinding
import com.isfandroid.whattowatch.R
import com.isfandroid.whattowatch.core.domain.model.Multi
import com.isfandroid.whattowatch.core.utils.Constants.IMAGE_BASE_URL

class MultiSmallAdapter(
    val onItemClicked: (Multi) -> Unit
): ListAdapter<Multi, MultiSmallAdapter.MultiViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiViewHolder {
        val binding = ItemMultiSmallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MultiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MultiViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MultiViewHolder(private val binding: ItemMultiSmallBinding)
    : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Multi) {
            with(binding) {
                root.setOnClickListener { onItemClicked.invoke(data) }

                Glide.with(itemView)
                    .load("${IMAGE_BASE_URL}${data.posterPath}")
                    .placeholder(R.drawable.placeholder_item_multi)
                    .into(ivCover)
                tvRating.text = String.format("%.1f", data.voteAverage)
            }
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Multi>() {
            override fun areItemsTheSame(oldItem: Multi, newItem: Multi): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Multi, newItem: Multi): Boolean {
                return oldItem == newItem
            }
        }
    }
}