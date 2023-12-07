package com.example.sosmeddicoding.ui.detailStory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.dicodingsocialmedia.databinding.ActivityDetailStoryBinding
import com.example.sosmeddicoding.data.database.entity.StoryResponseItem
import com.squareup.picasso.Picasso

class DetailStory : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item = intent.getParcelableExtra<StoryResponseItem>("detailPost")
        if(item != null) {
            val partialCreatedPost = item.createdAt?.take(10)

            binding.tvUserSlice.text = item.name
            binding.tvDetailDesc.text = item.description
//            binding.tvCreatedDetail.text = partialCreatedPost
            val imagePost: ImageView = binding.imageUser
            Picasso.get().load(item.photoUrl).into(imagePost)
//            binding.latitudeValue.text = item.lat.toString()
//            binding.longitudeValue.text = item.lon.toString()
        }
    }
}