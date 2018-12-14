package com.an.trailers.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.an.trailers.data.local.converter.*
import com.an.trailers.data.local.dao.MovieDao
import com.an.trailers.data.local.dao.TvDao
import com.an.trailers.data.local.entity.MovieEntity
import com.an.trailers.data.local.entity.TvEntity

@Database(entities = [MovieEntity::class, TvEntity::class], version = 1, exportSchema = false)
@TypeConverters(
    GenreListTypeConverter::class,
    VideoListTypeConverter::class,
    CreditResponseTypeConverter::class,
    MovieListTypeConverter::class,
    CastListTypeConverter::class,
    CrewListTypeConverter::class,
    StringListConverter::class,
    TvListTypeConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    abstract fun tvDao(): TvDao
}
