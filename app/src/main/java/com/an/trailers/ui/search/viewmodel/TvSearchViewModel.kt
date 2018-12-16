package com.an.trailers.ui.search.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.an.trailers.data.Resource
import com.an.trailers.data.local.dao.TvDao
import com.an.trailers.data.local.entity.TvEntity
import com.an.trailers.data.remote.api.TvApiService
import com.an.trailers.data.repository.TvRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import javax.inject.Inject

class TvSearchViewModel @Inject constructor(
    tvDao: TvDao,
    tvApiService: TvApiService) : ViewModel() {

    private val tvRepository: TvRepository = TvRepository(tvDao, tvApiService)
    private val tvsLiveData = MutableLiveData<Resource<List<TvEntity>>>()

    fun searchTv(text: String) {
        tvRepository.searchTvs(1, text)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { resource -> tvsLiveData.postValue(resource) }
    }

    fun getTvListLiveData() = tvsLiveData
}
