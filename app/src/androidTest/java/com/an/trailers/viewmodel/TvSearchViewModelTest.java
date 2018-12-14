package com.an.trailers.viewmodel;


import android.app.Application;
import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;
import android.support.test.InstrumentationRegistry;

import com.an.trailers.data.Resource;
import com.an.trailers.data.local.entity.TvEntity;
import com.an.trailers.ui.search.viewmodel.TvSearchViewModel;
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
public class TvSearchViewModelTest {


    private TvSearchViewModel tvSearchViewModel;

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
        tvSearchViewModel = new TvSearchViewModel(app);
    }

    @Test
    public void searchTvEntities() {

        tvSearchViewModel.getTvsLiveData().observeForever(observer);
        tvSearchViewModel.searchTv("friends");

        assert(tvSearchViewModel.getTvsLiveData().getValue().isLoading());
        assert(tvSearchViewModel.getTvsLiveData().getValue().data == MockTestUtil.mockTvList(MOVIES_POPULAR));
    }
}
