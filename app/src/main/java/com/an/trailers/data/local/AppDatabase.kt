package com.an.trailers.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.an.trailers.data.local.converter.*
import com.an.trailers.data.local.dao.MovieDao
import com.an.trailers.data.local.dao.TvDao
import com.an.trailers.data.local.entity.MovieEntity
import com.an.trailers.data.local.entity.TvEntity

@Database(
    entities = [MovieEntity::class, TvEntity::class],
    version = 1,
    exportSchema = false
)
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
