package com.isfandroid.whattowatch.core.ui.adapter.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.isfandroid.core.R
import com.isfandroid.core.databinding.ItemMultiLargeBinding
import com.isfandroid.whattowatch.core.domain.model.Multi
import com.isfandroid.whattowatch.core.utils.Constants

class MultiLargePagingAdapter(
    val onItemClicked: (Multi) -> Unit
): PagingDataAdapter<Multi, MultiLargePagingAdapter.MultiViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiViewHolder {
        val binding = ItemMultiLargeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MultiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MultiViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class MultiViewHolder(private val binding: ItemMultiLargeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Multi) {
            with(binding) {
                root.setOnClickListener { onItemClicked.invoke(data) }

                Glide.with(itemView)
                    .load("${Constants.IMAGE_BASE_URL}${data.posterPath}")
                    .placeholder(R.drawable.placeholder_item_multi)
                    .into(ivCover)
                tvYear.text = data.releaseDate ?: data.firstAirDate
                tvTitle.text = data.title ?: data.name
                tvRating.text = String.format("%.1f", data.voteAverage)
                tvMediaType.text = when (data.mediaType) {
                    Constants.MEDIA_TYPE_MOVIE -> "Movie"
                    Constants.MEDIA_TYPE_TV_SHOW -> "TV Show"
                    else -> "Unknown"
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Multi>() {
            override fun areItemsTheSame(oldItem: Multi, newItem: Multi): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Multi, newItem: Multi): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}