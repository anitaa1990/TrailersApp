package com.an.trailers.ui.main.viewmodel;

import android.arch.lifecycle.MutableLiveData;
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
    public MovieListViewModel(MovieDao movieDao, MovieApiService movieApiService) {
        movieRepository = new MovieRepository(movieDao, movieApiService);
    }

    private String type;
    private MovieRepository movieRepository;
    private MutableLiveData<Resource<List<MovieEntity>>> moviesLiveData = new MutableLiveData<>();

    public void setType(String type) {
        this.type = type;
    }

    public void loadMoreMovies(Long currentPage) {
        movieRepository.loadMoviesByType(currentPage, type)
                .doOnSubscribe(disposable -> addToDisposable(disposable))
                .subscribe(resource -> getMoviesLiveData().postValue(resource));
    }

    public boolean isLastPage() {
        return moviesLiveData.getValue() != null &&
                !moviesLiveData.getValue().data.isEmpty() ?
                moviesLiveData.getValue().data.get(0).isLastPage() :
                false;
    }

    public MutableLiveData<Resource<List<MovieEntity>>> getMoviesLiveData() {
        return moviesLiveData;
    }
}
