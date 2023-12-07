package com.example.sosmeddicoding.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.dicodingsocialmedia.databinding.ActivityRegisterBinding
import com.example.sosmeddicoding.data.model.ErrorResponse
import com.example.sosmeddicoding.data.repo.AuthRepo
import com.example.sosmeddicoding.data.service.ApiClient
import com.example.sosmeddicoding.ui.WelcomeActivity
import com.example.sosmeddicoding.utils.AuthPreferences
import com.example.sosmeddicoding.utils.CustomEditText
import com.example.sosmeddicoding.utils.dataStore
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authButton: Button
    private lateinit var editText: CustomEditText
    private lateinit var name: EditText
    private lateinit var viewModel: RegisterViewModel

    private lateinit var authPreferences: AuthPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        name = binding.name
        authButton = binding.buttonRegister
        editText = binding.password

        authPreferences = AuthPreferences.getInstance(application.dataStore)


        val authService = ApiClient.getApiService("")
        val authRepo = AuthRepo(authService, authPreferences)
        viewModel = ViewModelProvider(
            this,
            RegisterViewModelFactory(authRepo)
        ).get(RegisterViewModel::class.java)

        binding.buttonRegister.setOnClickListener {
            showLoading(true)
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            // Mencoba melakukan register
            lifecycleScope.launch {
                try {
                    val successResponse = viewModel.registerVM(name, email, password)
                    if (successResponse.error == false) {
                        showLoading(false)
                        startActivity(Intent(applicationContext, WelcomeActivity::class.java))
                            successResponse.message?.let { it1 -> showToast(it1) }
                        Toast.makeText(
                            this@RegisterActivity,
                            "Register Succcessful, please Log in with Your Account",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("RegisterActivity", "isSuccess: ${successResponse.error}")
                    }
                    Log.d("RegisterActivity", "isSuccess: ${successResponse.error}")
                } catch (e: HttpException) {
                    showLoading(false)
                    val jsonInString = e.response()?.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                    val errorMessage = errorBody.message
                    if (errorMessage != null) {
                        showToast(errorMessage)
                        Toast.makeText(
                            this@RegisterActivity,
                            "Register Failed, please register again",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }


        }

        binding.loginHere.setOnClickListener {
            startActivity(Intent(applicationContext, WelcomeActivity::class.java))
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.cardView.visibility = View.VISIBLE
        } else {
            binding.cardView.visibility = View.GONE
        }
    }
}