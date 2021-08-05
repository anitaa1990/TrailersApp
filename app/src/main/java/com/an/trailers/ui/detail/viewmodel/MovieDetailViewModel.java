package com.an.trailers.ui.detail.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.an.trailers.data.local.dao.MovieDao;
import com.an.trailers.data.local.entity.MovieEntity;
import com.an.trailers.data.remote.api.MovieApiService;
import com.an.trailers.data.repository.MovieRepository;
import javax.inject.Inject;

public class MovieDetailViewModel extends ViewModel {

    @Inject
    public MovieDetailViewModel(MovieDao movieDao, MovieApiService movieApiService) {
        movieRepository = new MovieRepository(movieDao, movieApiService);
    }

    private MovieRepository movieRepository;

    private MutableLiveData<MovieEntity> movieDetailsLiveData = new MutableLiveData<>();

    public void fetchMovieDetail(MovieEntity movieEntity) {
        movieRepository.fetchMovieDetails(movieEntity.getId())
        .subscribe(resource -> {
            if(resource.isLoaded()) getMovieDetailsLiveData().postValue(resource.data);
        });
    }

    public MutableLiveData<MovieEntity> getMovieDetailsLiveData() {
        return movieDetailsLiveData;
    }
}
