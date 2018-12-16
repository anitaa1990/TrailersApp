package com.an.trailers.ui.main.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.an.trailers.data.Resource
import com.an.trailers.data.local.dao.MovieDao
import com.an.trailers.data.local.entity.MovieEntity
import com.an.trailers.data.remote.api.MovieApiService
import com.an.trailers.data.repository.MovieRepository
import com.an.trailers.ui.base.BaseViewModel
import javax.inject.Inject

class MovieListViewModel@Inject constructor(
    movieDao: MovieDao,
    movieApiService: MovieApiService) : BaseViewModel() {

    private lateinit var type: String
    private val movieRepository: MovieRepository = MovieRepository(movieDao, movieApiService)
    private val moviesLiveData = MutableLiveData<Resource<List<MovieEntity>>>()


    fun setType(type: String) {
        this.type = type
    }

    fun loadMoreMovies(currentPage: Long) {
        movieRepository.loadMoviesByType(currentPage, type)
                .doOnSubscribe { disposable -> addToDisposable(disposable) }
                .subscribe { resource -> getMoviesLiveData().postValue(resource) }
    }

    fun isLastPage(): Boolean {
        if(moviesLiveData.value != null &&
                moviesLiveData.value!!.data != null &&
                !moviesLiveData.value!!.data!!.isEmpty()) {
            return moviesLiveData.value!!.data!![0].isLastPage()
        }

        return true
    }

    fun getMoviesLiveData() = moviesLiveData
}
