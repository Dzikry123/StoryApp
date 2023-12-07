package com.example.sosmeddicoding.ui.story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingsocialmedia.R
import com.example.dicodingsocialmedia.databinding.ActivityMainBinding
import com.example.sosmeddicoding.data.di.Injection
import com.example.sosmeddicoding.data.model.ErrorResponse
import com.example.sosmeddicoding.ui.WelcomeActivity
import com.example.sosmeddicoding.ui.camera.UploadActivity
import com.example.sosmeddicoding.ui.detailStory.DetailStory
import com.example.sosmeddicoding.ui.map.MapsActivity
import com.example.sosmeddicoding.ui.story.adapter.LoadingStateAdapter
import com.example.sosmeddicoding.utils.AuthPreferences
import com.example.sosmeddicoding.utils.dataStore
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class StoryActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var viewModel: StoryViewModel
    private lateinit var authPreferences: AuthPreferences

    private val adapter by lazy {
        StoryAdapter {
            val intent =  Intent(this@StoryActivity, DetailStory::class.java)
            intent.putExtra("detailPost", it)
            startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // NavBar
        drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        // Scroll to top after upload post
        val scrollToTop = intent.getBooleanExtra("scrollToTop", true)

        if (scrollToTop) {
            binding.recyclerView.smoothScrollToPosition(0)
        }



        // Check Token
        authPreferences = AuthPreferences.getInstance(application.dataStore)
        lifecycleScope.launch {
            authPreferences.getAuthToken.collect { savedToken ->
                if (savedToken == "") {
                    startActivity(Intent(applicationContext, WelcomeActivity::class.java))
                    authPreferences.clearToken()
                } else {
                    authPreferences.getAuthToken.collect {
                        this@StoryActivity
                    }
                }
            }
        }

        // Observe
        val storyRepo = Injection.provideRepository(this)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        viewModel = ViewModelProvider(
            this,
            StoryViewModelFactory(storyRepo)
        ).get(StoryViewModel::class.java)
        viewModel.allStories.observe(this) { response ->
            Log.d("Data masuk ke layar", response.toString() )
            if (response != null) {
                adapter.submitData(lifecycle, response)
                Log.d("Data masuk ke adapter", response.toString() )
            } else {
                val error = response.toString()
                showToast(error)
                Log.e("Data error", error)
            }
        }

        binding.upload.setOnClickListener {
            startActivity(Intent(applicationContext, UploadActivity::class.java))
        }

        setContentView(binding.root)

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> {
                lifecycleScope.launch {
                    try {
                        authPreferences.saveToken("")
                        startActivity(Intent(applicationContext, WelcomeActivity::class.java))
                    } catch (e: HttpException) {
                        val jsonInString = e.response()?.errorBody()?.string()
                        val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                        val errorMessage = errorBody.message
                        if (errorMessage != null) {
                            showToast(errorMessage)
                            Toast.makeText(
                                this@StoryActivity,
                                "Register Failed, please register again",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                }
            }
            R.id.nav_languange -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.nav_map -> {
                startActivity(Intent(applicationContext, MapsActivity::class.java))

            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onResume() {
        super.onResume()
        viewModel.allStories.observe(this) { response ->
            if (response != null) {
                adapter.submitData(lifecycle, response)
            } else {
                val error = response.toString()
                showToast(error)
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
