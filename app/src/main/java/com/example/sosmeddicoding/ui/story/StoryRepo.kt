package com.example.sosmeddicoding.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.sosmeddicoding.data.database.StoryDatabase
import com.example.sosmeddicoding.data.database.entity.StoryResponseItem
import com.example.sosmeddicoding.data.model.ResponseAddNewStory
import com.example.sosmeddicoding.data.model.ResponseGetAllStory
import com.example.sosmeddicoding.data.paging.StoryPagingSource
import com.example.sosmeddicoding.data.remoteMediator.StoryRemoteMediator
import com.example.sosmeddicoding.data.service.ApiService
import com.example.sosmeddicoding.utils.AuthPreferences
import com.example.sosmeddicoding.utils.ResultViewModel
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepo(private val apiService: ApiService, authPreferences: AuthPreferences, private val storyDatabase: StoryDatabase) {
    @OptIn(ExperimentalPagingApi::class)
    fun getAllStories() : LiveData<PagingData<StoryResponseItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }


    suspend fun getStoriesWithLocation(location: Int) : ResponseGetAllStory {
        return apiService.getStoriesWithLocation(location)
    }

    fun uploadImage(imageFile: File, description: String, lat: Double?, lon: Double?) = liveData {
        emit(ResultViewModel.Loading)
        val descRequestBody = description.toRequestBody("text/plain".toMediaType())
        val latRequestBody = lat?.let {
             it.toString().toRequestBody("text/plain".toMediaType())
        }
        val lonRequestBody = lon?.let {
            it.toString().toRequestBody("text/plain".toMediaType())
        }
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        try {
            val successResponse = apiService.postStory(multipartBody, descRequestBody, latRequestBody, lonRequestBody)
            emit(ResultViewModel.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ResponseAddNewStory::class.java)
            emit(errorResponse.message?.let { ResultViewModel.Error(it) })
        }

    }

    companion object {
        private var instance: StoryRepo? = null

        fun getInstance(apiService: ApiService, authPreferences: AuthPreferences, storyDatabase: StoryDatabase): StoryRepo {
            return instance ?: synchronized(this) {
                instance ?: StoryRepo(apiService, authPreferences, storyDatabase).also { instance = it }
            }
        }
    }
}


