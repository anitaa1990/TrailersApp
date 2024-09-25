package com.an.trailers.ui.detail.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.an.trailers.data.local.dao.TvDao
import com.an.trailers.data.local.entity.TvEntity
import com.an.trailers.data.remote.api.TvApiService
import com.an.trailers.data.repository.TvRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import javax.inject.Inject

class TvDetailViewModel @Inject constructor(
    tvDao: TvDao,
    tvApiService: TvApiService) : ViewModel() {

    private val tvRepository: TvRepository = TvRepository(tvDao, tvApiService)
    private val tvDetailsLiveData = MutableLiveData<TvEntity>()

    fun fetchMovieDetail(tvEntity: TvEntity) {
        tvRepository.fetchTvDetails(tvEntity.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { resource -> if (resource.isLoaded) tvDetailsLiveData.postValue(resource.data) }
    }

    fun getTvDetailsLiveData() = tvDetailsLiveData
}
