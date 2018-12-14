package com.an.trailers.ui.main.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.an.trailers.AppController;
import com.an.trailers.data.Resource;
import com.an.trailers.data.local.dao.TvDao;
import com.an.trailers.data.local.entity.TvEntity;
import com.an.trailers.data.remote.api.TvApiService;
import com.an.trailers.data.repository.TvRepository;
import com.an.trailers.ui.base.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

public class TvListViewModel extends ViewModel {

    @Inject
    public TvListViewModel(TvDao tvDao, TvApiService tvApiService) {
        tvRepository = new TvRepository(tvDao, tvApiService);
    }

    private TvRepository tvRepository;

    private MutableLiveData<Resource<List<TvEntity>>> tvsLiveData = new MutableLiveData<>();


    public void fetchTvs(String type) {
        tvRepository.loadTvsByType(type)
        .subscribe(resource -> getTvsLiveData().postValue(resource));
    }

    public MutableLiveData<Resource<List<TvEntity>>> getTvsLiveData() {
        return tvsLiveData;
    }
}
