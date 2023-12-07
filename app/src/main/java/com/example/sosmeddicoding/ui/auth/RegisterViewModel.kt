package com.example.sosmeddicoding.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sosmeddicoding.data.model.ResponseRegister
import com.example.sosmeddicoding.data.repo.AuthRepo


class RegisterViewModel(private val authRepo: AuthRepo) : ViewModel() {
     suspend fun registerVM(name: String, email: String, password: String): ResponseRegister {
         val response = authRepo.registerRepo(name, email, password)
         return response
    }
}


class RegisterViewModelFactory(private val repo: AuthRepo) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(repo) as T
        }
        throw IllegalAccessException("Unkwon ViewModel :" + modelClass.name)
    }
}