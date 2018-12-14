package com.an.trailers.ui.search.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TvSearchViewModel extends BaseViewModel {

    @Inject
    TvDao tvDao;

    @Inject
    TvApiService tvApiService;

    private TvRepository tvRepository;

    private MutableLiveData<Resource<List<TvEntity>>> tvsLiveData = new MutableLiveData<>();

    public TvSearchViewModel(@NonNull Application application) {
        super(application);
        ((AppController) application).getApiComponent().inject(this);
        tvRepository = new TvRepository(tvDao, tvApiService);
    }

    public void searchTv(String text) {
        tvRepository.searchTvs(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resource -> getTvsLiveData().postValue(resource));
    }

    public MutableLiveData<Resource<List<TvEntity>>> getTvsLiveData() {
        return tvsLiveData;
    }
}
