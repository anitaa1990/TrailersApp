package com.an.trailers.data.local.dao

import android.arch.persistence.room.*
import com.an.trailers.data.local.entity.MovieEntity
import io.reactivex.Flowable

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movies: List<MovieEntity>): LongArray

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: MovieEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateMovie(movie: MovieEntity): Int

    @Query("SELECT * FROM `MovieEntity` where id = :id")
    fun getMovieById(id: Long?): MovieEntity

    @Query("SELECT * FROM `MovieEntity` where id = :id")
    fun getMovieDetailById(id: Long?): Flowable<MovieEntity>

    @Query("SELECT * FROM `MovieEntity` where page = :page")
    fun getMoviesByPage(page: Long): List<MovieEntity>
}