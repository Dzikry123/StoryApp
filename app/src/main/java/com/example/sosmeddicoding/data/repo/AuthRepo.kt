package com.example.sosmeddicoding.data.repo

import com.example.sosmeddicoding.data.model.ResponseLogin
import com.example.sosmeddicoding.data.model.ResponseRegister
import com.example.sosmeddicoding.data.service.ApiService
import com.example.sosmeddicoding.utils.AuthPreferences

class AuthRepo(private val apiService: ApiService, private val authPreferences: AuthPreferences) {
    suspend fun registerRepo(name: String, email: String, password:String) : ResponseRegister {
        return apiService.register(name, email, password)
    }

    suspend fun loginRepo(email: String, password:String) : ResponseLogin {
        return apiService.login(email, password)
    }
}