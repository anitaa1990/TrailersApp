package com.an.trailers.viewmodel;

import android.app.Application;
import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;
import android.support.test.InstrumentationRegistry;
import com.an.trailers.data.local.entity.MovieEntity;
import com.an.trailers.ui.detail.viewmodel.MovieDetailViewModel;
import com.an.trailers.util.MockTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.an.trailers.AppConstants.MOVIES_POPULAR;

@RunWith(MockitoJUnitRunner.class)
public class MovieDetailViewModelTest {

    private MovieDetailViewModel movieDetailViewModel;

    @Mock
    Observer<MovieEntity> observer;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void init() {
        Application app =
                (Application) InstrumentationRegistry
                        .getTargetContext()
                        .getApplicationContext();
        movieDetailViewModel = new MovieDetailViewModel(app);
    }

    @Test
    public void loadMovieDetails() {

        MovieEntity movieEntity = MockTestUtil.mockMovieDetail(MOVIES_POPULAR);

        movieDetailViewModel.getMovieDetailsLiveData().observeForever(observer);
        movieDetailViewModel.fetchMovieDetail(movieEntity);

        assert(movieDetailViewModel.getMovieDetailsLiveData().getValue() == movieEntity);
        assert(movieDetailViewModel.getMovieDetailsLiveData().getValue()
                .getCrews().size() == movieEntity.getCrews().size());
        assert(movieDetailViewModel.getMovieDetailsLiveData().getValue()
                .getCasts().size() == movieEntity.getCasts().size());
    }
}
