package com.example.newsapi.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapi.repo.NewsRepo

class NewsViewModelProviderFactory(val repo: NewsRepo,val app : Application):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewmodel(repo,app) as T
    }
}