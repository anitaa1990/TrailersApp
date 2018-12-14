package com.an.trailers.ui.main.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
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

public class MovieListViewModel extends BaseViewModel {

    @Inject
    MovieDao movieDao;

    @Inject
    MovieApiService movieApiService;

    private MovieRepository movieRepository;

    private MutableLiveData<Resource<List<MovieEntity>>> moviesLiveData = new MutableLiveData<>();

    public MovieListViewModel(@NonNull Application application) {
        super(application);
        ((AppController) application).getApiComponent().inject(this);
        movieRepository = new MovieRepository(movieDao, movieApiService);
    }


    public void fetchMovies(String type) {
        movieRepository.loadMoviesByType(type)
        .subscribe(resource -> getMoviesLiveData().postValue(resource));
    }

    public MutableLiveData<Resource<List<MovieEntity>>> getMoviesLiveData() {
        return moviesLiveData;
    }
}
