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

    private lateinit var type: String
    private val tvRepository: TvRepository = TvRepository(tvDao, tvApiService)
    private val tvsLiveData = MutableLiveData<Resource<List<TvEntity>>>()

    fun setType(type: String) {
        this.type = type
    }


    fun loadMoreTvs(currentPage: Long) {
        tvRepository.loadTvsByType(currentPage, type)
                .doOnSubscribe { disposable -> addToDisposable(disposable) }
                .subscribe { resource -> getTvListLiveData().postValue(resource) }
    }

    fun isLastPage(): Boolean {
        if(getTvListLiveData().value != null &&
                getTvListLiveData().value!!.data != null &&
                !getTvListLiveData().value!!.data!!.isEmpty()) {
            return getTvListLiveData().value!!.data!![0].isLastPage()
        }

        return true
    }

    fun getTvListLiveData() = tvsLiveData
}
