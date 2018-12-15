package com.an.trailers.ui.main.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.an.trailers.data.Resource
import com.an.trailers.data.local.dao.TvDao
import com.an.trailers.data.local.entity.TvEntity
import com.an.trailers.data.remote.api.TvApiService
import com.an.trailers.data.repository.TvRepository
import com.an.trailers.ui.base.BaseViewModel

import javax.inject.Inject

class TvListViewModel @Inject constructor(
                      tvDao: TvDao,
                      tvApiService: TvApiService) : BaseViewModel() {

    private val tvRepository: TvRepository = TvRepository(tvDao, tvApiService)

    private val tvsLiveData = MutableLiveData<Resource<List<TvEntity>>>()


    fun fetchTvs(type: String) {
        tvRepository.loadTvsByType(type)
            .doOnSubscribe { addToDisposable(it) }
            .subscribe { resource -> tvsLiveData.postValue(resource) }
    }

    fun getTvListLiveData() = tvsLiveData
}
