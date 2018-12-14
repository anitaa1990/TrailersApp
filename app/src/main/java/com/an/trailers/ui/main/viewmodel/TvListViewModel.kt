package com.an.trailers.ui.main.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.an.trailers.data.Resource
import com.an.trailers.data.local.dao.TvDao
import com.an.trailers.data.local.entity.TvEntity
import com.an.trailers.data.remote.api.TvApiService
import com.an.trailers.data.repository.TvRepository

import javax.inject.Inject

class TvListViewModel @Inject constructor(
                      tvDao: TvDao,
                      tvApiService: TvApiService) : ViewModel() {

    private val tvRepository: TvRepository = TvRepository(tvDao, tvApiService)

    private val tvsLiveData = MutableLiveData<Resource<List<TvEntity>>>()


    fun fetchTvs(type: String) {
        tvRepository.loadTvsByType(type)
            .subscribe { resource -> tvsLiveData.postValue(resource) }
    }

    fun getTvListLiveData() = tvsLiveData
}
