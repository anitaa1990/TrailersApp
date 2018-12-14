package com.an.trailers.ui.detail.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import com.an.trailers.AppController;
import com.an.trailers.data.local.dao.TvDao;
import com.an.trailers.data.local.entity.TvEntity;
import com.an.trailers.data.remote.api.TvApiService;
import com.an.trailers.data.repository.TvRepository;
import com.an.trailers.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TvDetailViewModel extends ViewModel {

    @Inject
    public TvDetailViewModel(TvDao tvDao, TvApiService tvApiService) {
        tvRepository = new TvRepository(tvDao, tvApiService);
    }

    private TvRepository tvRepository;

    private MutableLiveData<TvEntity> tvDetailsLiveData = new MutableLiveData<>();


    public void fetchMovieDetail(TvEntity tvEntity) {
        tvRepository.fetchTvDetails(tvEntity.getId())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(resource -> {
            if(resource.isLoaded()) getTvDetailsLiveData().postValue(resource.data);
        });
    }

    public MutableLiveData<TvEntity> getTvDetailsLiveData() {
        return tvDetailsLiveData;
    }
}
