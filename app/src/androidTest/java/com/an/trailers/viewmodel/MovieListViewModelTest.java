package com.an.trailers.viewmodel;

import android.app.Application;
import com.an.trailers.data.Resource;
import com.an.trailers.data.local.entity.MovieEntity;
import com.an.trailers.ui.main.viewmodel.MovieListViewModel;
import com.an.trailers.util.MockTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.List;
import static com.an.trailers.AppConstants.MOVIES_POPULAR;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.test.core.app.ApplicationProvider;

@RunWith(MockitoJUnitRunner.class)
public class MovieListViewModelTest {

    private MovieListViewModel movieListViewModel;

    @Mock
    Observer<Resource<List<MovieEntity>>> observer;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void init() {
        Application app =
                (Application) ApplicationProvider
                        .getApplicationContext();
        movieListViewModel = new MovieListViewModel(app);
    }

    @Test
    public void fetchMovies() {
        movieListViewModel.getMoviesLiveData().observeForever(observer);
        movieListViewModel.fetchMovies(MOVIES_POPULAR);

        assert(movieListViewModel.getMoviesLiveData().getValue().isLoading());
        assert(movieListViewModel.getMoviesLiveData().getValue().data == MockTestUtil.mockMovieList(MOVIES_POPULAR));
    }
}
