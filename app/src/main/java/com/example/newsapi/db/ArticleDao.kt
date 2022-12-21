package com.example.newsapi.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapi.model.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article):Long

    @Query("Select * From articles")
    fun getAllArticles():LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}