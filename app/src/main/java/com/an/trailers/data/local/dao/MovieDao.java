package com.an.trailers.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.an.trailers.data.local.entity.MovieEntity;
import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertMovies(List<MovieEntity> movies);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMovie(MovieEntity movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateMovie(MovieEntity movie);

    @Query("SELECT * FROM `MovieEntity` where id = :id")
    MovieEntity getMovieById(Long id);

    @Query("SELECT * FROM `MovieEntity` where id = :id")
    Flowable<MovieEntity> getMovieDetailById(Long id);

    @Query("SELECT * FROM `MovieEntity` where page = :page")
    List<MovieEntity> getMoviesByPage(Long page);
}