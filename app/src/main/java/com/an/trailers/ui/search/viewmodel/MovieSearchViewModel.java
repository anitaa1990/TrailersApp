package com.an.trailers.ui.search.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.an.trailers.AppController;
import com.an.trailers.data.Resource;
import com.an.trailers.data.local.dao.MovieDao;
import com.an.trailers.data.local.entity.MovieEntity;
import com.an.trailers.data.remote.api.MovieApiService;
import com.an.trailers.data.repository.MovieRepository;
import com.an.trailers.ui.base.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieSearchViewModel extends ViewModel {

    @Inject
    public MovieSearchViewModel(MovieDao movieDao, MovieApiService movieApiService) {
        movieRepository = new MovieRepository(movieDao, movieApiService);
    }

    private MovieRepository movieRepository;

    private MutableLiveData<Resource<List<MovieEntity>>> moviesLiveData = new MutableLiveData<>();

    public void searchMovie(String text) {
        movieRepository.searchMovies(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resource -> getMoviesLiveData().postValue(resource));
    }

    public MutableLiveData<Resource<List<MovieEntity>>> getMoviesLiveData() {
        return moviesLiveData;
    }
}
