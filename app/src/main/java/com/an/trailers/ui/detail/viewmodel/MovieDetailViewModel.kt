package com.an.trailers.ui.detail.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.an.trailers.data.local.dao.MovieDao
import com.an.trailers.data.local.entity.MovieEntity
import com.an.trailers.data.remote.api.MovieApiService
import com.an.trailers.data.repository.MovieRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
    movieDao: MovieDao,
    movieApiService: MovieApiService) : ViewModel() {

    private val movieRepository: MovieRepository = MovieRepository(movieDao, movieApiService)
    private val movieDetailsLiveData = MutableLiveData<MovieEntity>()

    fun fetchMovieDetail(movieEntity: MovieEntity) {
        movieRepository.fetchMovieDetails(movieEntity.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { resource -> if (resource.isLoaded) movieDetailsLiveData.postValue(resource.data) }
    }

    fun getMovieDetailsLiveData() = movieDetailsLiveData
}
