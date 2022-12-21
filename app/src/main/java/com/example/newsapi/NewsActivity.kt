package com.example.newsapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapi.databinding.ActivityNewsBinding
import com.example.newsapi.db.ArticleDatabse
import com.example.newsapi.repo.NewsRepo
import com.example.newsapi.ui.NewsViewModelProviderFactory
import com.example.newsapi.ui.NewsViewmodel

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    lateinit var viewmodel: NewsViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController


        val repo = NewsRepo(ArticleDatabse(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(repo,application)

        viewmodel  = ViewModelProvider(this,viewModelProviderFactory).get(NewsViewmodel::class.java)
        binding.bottomNavigationView.setupWithNavController(navController)

    }
}