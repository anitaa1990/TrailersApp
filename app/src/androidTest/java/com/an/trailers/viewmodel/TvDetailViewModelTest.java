package com.an.trailers.viewmodel;

import android.app.Application;
import com.an.trailers.data.local.entity.TvEntity;
import com.an.trailers.ui.detail.viewmodel.MovieDetailViewModel;
import com.an.trailers.ui.detail.viewmodel.TvDetailViewModel;
import com.an.trailers.util.MockTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.an.trailers.AppConstants.MOVIES_POPULAR;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.test.core.app.ApplicationProvider;

@RunWith(MockitoJUnitRunner.class)
public class TvDetailViewModelTest {

    private TvDetailViewModel tvDetailViewModel;

    @Mock
    Observer<TvEntity> observer;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void init() {
        Application app =
                (Application) ApplicationProvider
                        .getApplicationContext();
        tvDetailViewModel = new TvDetailViewModel(app);
    }

    @Test
    public void loadTvDetails() {

        TvEntity tvEntity = MockTestUtil.mockTvDetail(MOVIES_POPULAR);

        tvDetailViewModel.getTvDetailsLiveData().observeForever(observer);
        tvDetailViewModel.fetchMovieDetail(tvEntity);

        assert(tvDetailViewModel.getTvDetailsLiveData().getValue() == tvEntity);
        assert(tvDetailViewModel.getTvDetailsLiveData().getValue()
                .getCrews().size() == tvEntity.getCrews().size());
        assert(tvDetailViewModel.getTvDetailsLiveData().getValue()
                .getCasts().size() == tvEntity.getCasts().size());
    }
}
