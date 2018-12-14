package com.an.trailers.ui.main.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.an.trailers.data.Resource
import com.an.trailers.data.local.dao.MovieDao
import com.an.trailers.data.local.entity.MovieEntity
import com.an.trailers.data.remote.api.MovieApiService
import com.an.trailers.data.repository.MovieRepository
import javax.inject.Inject

class MovieListViewModel@Inject constructor(
    movieDao: MovieDao,
    movieApiService: MovieApiService) : ViewModel() {

    private val movieRepository: MovieRepository = MovieRepository(movieDao, movieApiService)
    private val moviesLiveData = MutableLiveData<Resource<List<MovieEntity>>>()


    fun fetchMovies(type: String) {
        movieRepository.loadMoviesByType(type)
            .subscribe { resource -> moviesLiveData.postValue(resource) }
    }

    fun getMoviesLiveData() = moviesLiveData
}
