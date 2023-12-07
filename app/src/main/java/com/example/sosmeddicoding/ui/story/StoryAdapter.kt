package com.example.sosmeddicoding.ui.story

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.dicodingsocialmedia.databinding.ListPostBinding
import com.example.sosmeddicoding.data.database.entity.StoryResponseItem

class StoryAdapter(private val onItemClick: (StoryResponseItem) -> Unit) :
    PagingDataAdapter<StoryResponseItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data, onItemClick)
        }
    }

    class MyViewHolder(private val binding: ListPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoryResponseItem, onItemClick: (StoryResponseItem) -> Unit) {
            binding.imageUser.load(data.photoUrl)
            val partialDescription = data.description?.take(50)
            binding.tvUserSlice.text = data.name
            binding.tvDesc.text = partialDescription
            binding.root.setOnClickListener { onItemClick(data) }
        }
    }

    companion object {
         val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryResponseItem>() {
            override fun areItemsTheSame(oldItem: StoryResponseItem, newItem: StoryResponseItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: StoryResponseItem, newItem: StoryResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}



