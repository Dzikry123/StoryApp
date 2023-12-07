package com.example.sosmeddicoding.ui.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sosmeddicoding.ui.story.StoryRepo
import java.io.File

class UploadViewModel(private val storyRepo: StoryRepo): ViewModel() {
    fun uploadImage(file: File, desc: String, lat: Double?, lon: Double?) =
        if ( lat != null && lon != null) {
            storyRepo.uploadImage(file, desc, lat, lon)
        } else {
            storyRepo.uploadImage(file, desc, null, null)
        }
}

class UploadViewModelFactory(private val storyRepo: StoryRepo) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UploadViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UploadViewModel(storyRepo) as T
        }
        throw IllegalAccessException("Unkwon ViewModel :" + modelClass.name)
    }
}