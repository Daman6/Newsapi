package com.example.newsapi.db

import android.content.Context
import androidx.room.*
import com.example.newsapi.model.Article

@Database(
    entities = [Article::class],
    version = 4,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class ArticleDatabse : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

    companion object{

        private var instance: ArticleDatabse? = null
        private val lock= Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock)
        {
            instance ?: createDatabase(context).also{ instance = it}
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabse::class.java,
                "article_db.db"
            ).fallbackToDestructiveMigration()
                .build()

    }
}