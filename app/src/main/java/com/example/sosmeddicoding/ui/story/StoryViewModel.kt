package com.example.sosmeddicoding.ui.story


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.sosmeddicoding.data.database.entity.StoryResponseItem
import com.example.sosmeddicoding.data.model.ResponseGetAllStory

class StoryViewModel(private val storyRepo: StoryRepo) : ViewModel() {
    val allStories: LiveData<PagingData<StoryResponseItem>> =
        storyRepo.getAllStories().cachedIn(viewModelScope)


    val allStoriesWithLocation: LiveData<ResponseGetAllStory> = liveData {
        try{
            val location = 1
            val result = storyRepo.getStoriesWithLocation(location)
            emit(result)
        } catch (e:Exception) {
            emit(ResponseGetAllStory(error = true, message = "Terjadi kesalahan ${e.message}", listStory = emptyList()))
        }
    }
}

class StoryViewModelFactory(private val storyRepo: StoryRepo) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryViewModel(storyRepo) as T
        }
        throw IllegalAccessException("Unkwon ViewModel :" + modelClass.name)
    }
}