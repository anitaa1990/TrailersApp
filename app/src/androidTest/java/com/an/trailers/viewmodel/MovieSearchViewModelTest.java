package com.an.trailers.viewmodel;


import android.app.Application;
import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;
import android.support.test.InstrumentationRegistry;

import com.an.trailers.data.Resource;
import com.an.trailers.data.local.entity.MovieEntity;
import com.an.trailers.ui.search.viewmodel.MovieSearchViewModel;
import com.an.trailers.util.MockTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static com.an.trailers.AppConstants.MOVIES_POPULAR;

@RunWith(MockitoJUnitRunner.class)
public class MovieSearchViewModelTest {


    private MovieSearchViewModel movieSearchViewModel;

    @Mock
    Observer<Resource<List<MovieEntity>>> observer;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void init() {
        Application app =
                (Application) InstrumentationRegistry
                        .getTargetContext()
                        .getApplicationContext();
        movieSearchViewModel = new MovieSearchViewModel(app);
    }

    @Test
    public void searchMovies() {

        movieSearchViewModel.getMoviesLiveData().observeForever(observer);
        movieSearchViewModel.searchMovie("thor");

        assert(movieSearchViewModel.getMoviesLiveData().getValue().isLoading());
        assert(movieSearchViewModel.getMoviesLiveData().getValue().data == MockTestUtil.mockMovieList(MOVIES_POPULAR));
    }
}
