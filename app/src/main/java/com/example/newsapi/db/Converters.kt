package com.example.newsapi.db

import androidx.room.TypeConverter
import com.example.newsapi.model.Source

class Converters {
    @TypeConverter
    fun fromSource(source:com.example.newsapi.model.Source):String
    {
        return source.name
    }

    @TypeConverter
    fun toSource(name:String) :Source{
        return Source(name,name)
    }
}