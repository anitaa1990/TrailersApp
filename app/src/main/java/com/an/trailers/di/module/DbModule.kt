package com.an.trailers.di.module

import android.app.Application
import android.arch.persistence.room.Room
import com.an.trailers.data.local.AppDatabase
import com.an.trailers.data.local.dao.MovieDao
import com.an.trailers.data.local.dao.TvDao
import dagger.Module
import dagger.Provides

import javax.inject.Singleton

@Module
class DbModule {

    @Provides
    @Singleton
    internal fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application, AppDatabase::class.java, "Entertainment.db")
            .allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    internal fun provideMovieDao(appDatabase: AppDatabase): MovieDao {
        return appDatabase.movieDao()
    }


    @Provides
    @Singleton
    internal fun provideTvDao(appDatabase: AppDatabase): TvDao {
        return appDatabase.tvDao()
    }
}
