package com.example.sosmeddicoding.data.di

import android.content.Context
import com.example.sosmeddicoding.data.database.StoryDatabase
import com.example.sosmeddicoding.data.service.ApiClient
import com.example.sosmeddicoding.ui.story.StoryRepo
import com.example.sosmeddicoding.utils.AuthPreferences
import com.example.sosmeddicoding.utils.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepo {
        val pref = AuthPreferences.getInstance(context.dataStore)
        val userToken = runBlocking { pref.getAuthToken.first() }
        val apiService = ApiClient.getApiService(userToken)
        val db = StoryDatabase.getDatabase(context)
        return StoryRepo.getInstance(apiService, pref, db)
    }
}