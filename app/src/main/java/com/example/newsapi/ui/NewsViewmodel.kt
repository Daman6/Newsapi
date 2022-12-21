package com.example.newsapi.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapi.NewsApplication
import com.example.newsapi.model.Article
import com.example.newsapi.model.NewsResponse
import com.example.newsapi.repo.NewsRepo
import com.example.newsapi.util.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class NewsViewmodel(val repo : NewsRepo,val app : Application ) : AndroidViewModel(app){

    val breakingNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1

    val searchNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode:String) = viewModelScope.launch {
//        breakingNews.postValue(Resource.Loading())
//        val response = repo.getBreakingNews(countryCode,breakingNewsPage)
//        breakingNews.postValue(handleBreakingNewsResponse(response))
        safeBreakingNewsCall(countryCode)
    }

    fun searchNews(searchQuery:String) = viewModelScope.launch {
//        searchNews.postValue(Resource.Loading())
//        val response = repo.SearchNews(searchQuery,searchNewsPage)
//        searchNews.postValue(handleSearchNewsResponse(response))
        safeSearchNewsCall(searchQuery)
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {newsResponse ->
                return Resource.Success(newsResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {newsResponse ->
                return Resource.Success(newsResponse)
            }
        }
        return Resource.Error(response.message())
    }

   fun saveArticle(article: Article){
       viewModelScope.launch {
           repo.upsert(article)
       }
   }
   fun getSavedNews()= repo.getSavedNews()

   fun deleteArticle(article: Article){
       viewModelScope.launch {
           repo.deleteArticle(article)
       }
   }

   private suspend fun safeBreakingNewsCall(countryCode: String){
       breakingNews.postValue(Resource.Loading())
       try {
           if (hasInternetConnectivity()){
               val response = repo.getBreakingNews(countryCode,breakingNewsPage)
               breakingNews.postValue(handleBreakingNewsResponse(response))
           }else{
               breakingNews.postValue(Resource.Error("No Internet"))
           }
       }catch (t :Throwable){
            when(t){
                is IOException ->  breakingNews.postValue(Resource.Error("Network Failure"))
                else ->breakingNews.postValue(Resource.Error("Conversion Error"))

            }
       }
   }
    private suspend fun safeSearchNewsCall(searchQuery: String){
        searchNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnectivity()){
                val response = repo.SearchNews(searchQuery,searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            }else{
                searchNews.postValue(Resource.Error("No Internet"))
            }
        }catch (t :Throwable){
            when(t){
                is IOException ->  searchNews.postValue(Resource.Error("Network Failure"))
                else ->searchNews.postValue(Resource.Error("Conversion Error"))

            }
        }
    }

   fun hasInternetConnectivity():Boolean{
       val connectivityManager = getApplication<NewsApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
       if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
           val activeNetwork = connectivityManager.activeNetwork ?: return false
           val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)?:return false
           return when{
               capabilities.hasTransport(TRANSPORT_WIFI) ->true
               capabilities.hasTransport(TRANSPORT_CELLULAR) ->true
               capabilities.hasTransport(TRANSPORT_ETHERNET) ->true
               else->false
               }
           }else{
               connectivityManager.activeNetworkInfo?.run {
                   return when(type){
                       TYPE_WIFI->true
                       TYPE_MOBILE->true
                       TYPE_ETHERNET->true
                       else-> false
                   }
               }
           }
       return false
       }

   }
