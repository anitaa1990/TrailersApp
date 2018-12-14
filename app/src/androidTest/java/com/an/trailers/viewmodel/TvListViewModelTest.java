package com.an.trailers.viewmodel;

import android.app.Application;
import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;
import android.support.test.InstrumentationRegistry;

import com.an.trailers.data.Resource;
import com.an.trailers.data.local.dao.TvDao;
import com.an.trailers.data.local.entity.TvEntity;
import com.an.trailers.data.remote.api.TvApiService;
import com.an.trailers.data.remote.model.TvApiResponse;
import com.an.trailers.ui.main.viewmodel.TvListViewModel;
import com.an.trailers.util.MockTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

import static com.an.trailers.AppConstants.MOVIES_POPULAR;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TvListViewModelTest {

    private TvListViewModel tvListViewModel;

    @Mock
    Observer<Resource<List<TvEntity>>> observer;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void init() {
        Application app =
                (Application) InstrumentationRegistry
                        .getTargetContext()
                        .getApplicationContext();
        tvListViewModel = new TvListViewModel(app);
    }

    @Test
    public void loadMovieList() {

        tvListViewModel.getTvsLiveData().observeForever(observer);
        tvListViewModel.fetchTvs(MOVIES_POPULAR);

        assert(tvListViewModel.getTvsLiveData().getValue().isLoading());
        assert(tvListViewModel.getTvsLiveData().getValue().data == MockTestUtil.mockTvList(MOVIES_POPULAR));
    }
}