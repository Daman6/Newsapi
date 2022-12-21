package com.example.newsapi.repo

import com.example.newsapi.api.RetrofitInstance
import com.example.newsapi.db.ArticleDatabse
import com.example.newsapi.model.Article
import retrofit2.Retrofit

class NewsRepo(
    val db :ArticleDatabse
) {

    suspend fun getBreakingNews(countryCode:String,pagenumber:Int)=
        RetrofitInstance.api.getBreakingNews(countryCode,pagenumber)

    suspend fun SearchNews(searchQuery:String,pagenumber: Int)=
        RetrofitInstance.api.searchForNews(searchQuery,pagenumber)

    suspend fun upsert(article:Article)=db.getArticleDao().upsert(article)

    fun getSavedNews()=db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article)= db.getArticleDao().deleteArticle(article)
}