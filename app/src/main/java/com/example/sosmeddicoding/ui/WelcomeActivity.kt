package com.example.sosmeddicoding.ui


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.dicodingsocialmedia.databinding.ActivityWelcomeBinding
import com.example.sosmeddicoding.data.model.ErrorResponse
import com.example.sosmeddicoding.data.repo.AuthRepo
import com.example.sosmeddicoding.data.service.ApiClient
import com.example.sosmeddicoding.ui.auth.RegisterActivity
import com.example.sosmeddicoding.ui.story.StoryActivity
import com.example.sosmeddicoding.utils.AuthPreferences
import com.example.sosmeddicoding.utils.CustomEditText
import com.example.sosmeddicoding.utils.dataStore
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var authButton: Button
    private lateinit var editText: CustomEditText

    private lateinit var viewModel: WelcomeViewModel
    private lateinit var authPreferences: AuthPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authButton = binding.buttonLogin
        editText = binding.password


        binding.registerHere.setOnClickListener {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
        }

        // Check Token
        authPreferences = AuthPreferences.getInstance(application.dataStore)
        lifecycleScope.launch {
            authPreferences.getAuthToken.collect { savedToken ->
                if (savedToken != "") {
                    startActivity(Intent(applicationContext, StoryActivity::class.java))
                } else {
                    this@WelcomeActivity
                }
            }
        }

        authButton.setOnClickListener {
            showLoading(true)
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            val authService = ApiClient.getApiService("")
            val authRepo = AuthRepo(authService, authPreferences)
            viewModel = ViewModelProvider(
                this,
                WelcomeViewModelFactory(authRepo)
            ).get(WelcomeViewModel::class.java)


            lifecycleScope.launch {
                try {
                    val successLogin = viewModel.loginVM(email, password)
                    if (successLogin.error == false && successLogin.loginResult != null) {
                        val token = successLogin.loginResult.token
                        if (token != null) {
                            authPreferences.saveToken(token)
                        }
                        showLoading(false)
                        showToast(successLogin.message.toString())
                        startActivity(Intent(applicationContext, StoryActivity::class.java))

                    }
                } catch (e: HttpException) {
                    showLoading(false)
                    val jsonInString = e.response()?.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                    val errorMessage = errorBody.message
                    if (errorMessage != null) {
                        showToast(errorMessage)
                        Toast.makeText(
                            this@WelcomeActivity,
                            "Login Failed, please register again",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }


    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.cardView.visibility = View.VISIBLE
        } else {
            binding.cardView.visibility = View.GONE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}