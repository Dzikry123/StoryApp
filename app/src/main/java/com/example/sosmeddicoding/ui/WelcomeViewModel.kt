package com.example.sosmeddicoding.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sosmeddicoding.data.model.ResponseLogin
import com.example.sosmeddicoding.data.repo.AuthRepo

class WelcomeViewModel(private val authRepo: AuthRepo) : ViewModel() {
    suspend fun loginVM(email: String, password: String): ResponseLogin {
        val response = authRepo.loginRepo(email, password)
        return response
    }
}

class WelcomeViewModelFactory(private val repo: AuthRepo) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WelcomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WelcomeViewModel(repo) as T
        }
        throw IllegalAccessException("Unkwon ViewModel :" + modelClass.name)
    }
}