package com.an.trailers.ui.main.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import com.an.trailers.data.Resource;
import com.an.trailers.data.local.dao.TvDao;
import com.an.trailers.data.local.entity.TvEntity;
import com.an.trailers.data.remote.api.TvApiService;
import com.an.trailers.data.repository.TvRepository;
import com.an.trailers.ui.base.BaseViewModel;
import java.util.List;
import javax.inject.Inject;

public class TvListViewModel extends BaseViewModel {

    @Inject
    public TvListViewModel(TvDao tvDao, TvApiService tvApiService) {
        tvRepository = new TvRepository(tvDao, tvApiService);
    }

    private String type;
    private TvRepository tvRepository;
    private MutableLiveData<Resource<List<TvEntity>>> tvsLiveData = new MutableLiveData<>();

    public void setType(String type) {
        this.type = type;
    }

    public void loadMoreTvs(Long currentPage) {
        tvRepository.loadTvsByType(currentPage, type)
                .doOnSubscribe(disposable -> addToDisposable(disposable))
                .subscribe(resource -> getTvsLiveData().postValue(resource));
    }

    public boolean isLastPage() {
        return tvsLiveData.getValue() != null &&
                !tvsLiveData.getValue().data.isEmpty() ?
                tvsLiveData.getValue().data.get(0).isLastPage() :
                false;
    }

    public MutableLiveData<Resource<List<TvEntity>>> getTvsLiveData() {
        return tvsLiveData;
    }
}
