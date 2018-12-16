package com.an.trailers.ui.search.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.an.trailers.data.Resource
import com.an.trailers.data.local.dao.MovieDao
import com.an.trailers.data.local.entity.MovieEntity
import com.an.trailers.data.remote.api.MovieApiService
import com.an.trailers.data.repository.MovieRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import javax.inject.Inject

class MovieSearchViewModel @Inject constructor(
    movieDao: MovieDao,
    movieApiService: MovieApiService) : ViewModel() {


    private val movieRepository: MovieRepository = MovieRepository(movieDao, movieApiService)
    private val moviesLiveData = MutableLiveData<Resource<List<MovieEntity>>>()

    fun searchMovie(text: String) {
        movieRepository.searchMovies(1, text)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { resource -> moviesLiveData.postValue(resource) }
    }

    fun getMoviesLiveData() = moviesLiveData
}
