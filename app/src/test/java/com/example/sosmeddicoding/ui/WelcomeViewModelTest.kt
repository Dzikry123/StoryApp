package com.example.sosmeddicoding.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.sosmeddicoding.DataDummy
import com.example.sosmeddicoding.MainDispatcherRule
import com.example.sosmeddicoding.data.model.ResponseLogin
import com.example.sosmeddicoding.data.repo.AuthRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WelcomeViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock private lateinit var authRepo: AuthRepo

    @Test
    fun `when login is success`() = runTest {
        val dummyLogin = DataDummy.generateDummyLoginResponse()
        val email = "arsene@gmail.com"
        val password = "12345678"

        Mockito.`when`(authRepo.loginRepo(email, password)).thenReturn(dummyLogin)

        val welcomeViewModel = WelcomeViewModel(authRepo)
        val response = welcomeViewModel.loginVM(email, password)

        assertEquals(dummyLogin,response )
    }

    @Test
    fun `when login is failed`() = runTest {
        val errorResponse = ResponseLogin(
            error = true,
            message = "User not found"
        )
        val email = "arseneasdasdsaasas@gmail.com"
        val password = "12345678"

        Mockito.`when`(authRepo.loginRepo(email, password)).thenReturn(errorResponse)

        val welcomeViewModel = WelcomeViewModel(authRepo)
        val response = welcomeViewModel.loginVM(email, password)

        assertEquals(errorResponse,response )
    }
}