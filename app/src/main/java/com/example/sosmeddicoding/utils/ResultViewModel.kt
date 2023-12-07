package com.example.sosmeddicoding.utils


sealed class ResultViewModel<out R> private constructor() {
    data class Success<out T>(val data: T) : ResultViewModel<T>()
    data class Error(val error: String) : ResultViewModel<Nothing>()
    object Loading : ResultViewModel<Nothing>()
}