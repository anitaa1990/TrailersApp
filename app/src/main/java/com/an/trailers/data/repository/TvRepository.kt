package com.an.trailers.data.repository


import com.an.trailers.data.NetworkBoundResource
import com.an.trailers.data.Resource
import com.an.trailers.data.local.dao.TvDao
import com.an.trailers.data.local.entity.TvEntity
import com.an.trailers.data.remote.api.TvApiService
import com.an.trailers.data.remote.model.CreditResponse
import com.an.trailers.data.remote.model.TvApiResponse
import com.an.trailers.data.remote.model.VideoResponse
import com.an.trailers.utils.AppUtils
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.functions.Function4
import java.util.*

import javax.inject.Singleton

@Singleton
class TvRepository(
    private val tvDao: TvDao,
    private val tvApiService: TvApiService
) {

    fun loadTvsByType(page: Long,
                      type: String): Observable<Resource<List<TvEntity>>> {
        return object : NetworkBoundResource<List<TvEntity>, TvApiResponse>() {

            override fun saveCallResult(item: TvApiResponse) {
                val tvEntities = ArrayList<TvEntity>()
                for (tvEntity in item.results) {
                    val storedEntity = tvDao.getTvById(tvEntity.id)
                    if(storedEntity == null) {
                        tvEntity.categoryTypes = Arrays.asList(type)
                    } else {
                        val categories: MutableList<String> = mutableListOf()
                        if(storedEntity.categoryTypes != null) categories.addAll(storedEntity.categoryTypes!!)
                        categories.add(type)
                        tvEntity.categoryTypes = categories
                    }

                    tvEntity.page = item.page
                    tvEntity.totalPages = item.total_pages
                    tvEntities.add(tvEntity)
                }
                tvDao.insertTvList(tvEntities)
            }

            override fun shouldFetch(): Boolean {
                return true
            }

            override fun loadFromDb(): Flowable<List<TvEntity>> {
                val movieEntities = tvDao.getTvsByPage(page)
                return if (movieEntities == null || movieEntities.isEmpty()) {
                    Flowable.empty()
                } else Flowable.just(AppUtils.getTvsByType(type, movieEntities))
            }

            override fun createCall(): Observable<Resource<TvApiResponse>> {
                return tvApiService.fetchTvListByType(type, page)
                    .flatMap { tvApiResponse ->
                        Observable.just(
                            if (tvApiResponse == null)
                                Resource.error("", TvApiResponse(1, emptyList(), 0, 1))
                            else
                                Resource.success(tvApiResponse)
                        )
                    }
            }
        }.getAsObservable()
    }


    fun fetchTvDetails(tvId: Long): Observable<Resource<TvEntity>> {
        return object : NetworkBoundResource<TvEntity, TvEntity>() {
            override fun saveCallResult(item: TvEntity) {
                val tvEntity: TvEntity = tvDao.getTvById(tvId)
                if(null == tvEntity) tvDao.insertTv(item)
                else {
                    item.page = tvEntity.page
                    item.totalPages = tvEntity.totalPages
                    item.categoryTypes = tvEntity.categoryTypes
                    tvDao.updateTv(item)
                }
            }

            override fun shouldFetch(): Boolean {
                return true
            }

            override fun loadFromDb(): Flowable<TvEntity> {
                val tvEntity: TvEntity = tvDao.getTvById(tvId)
                if(null == tvEntity) return Flowable.empty()
                return Flowable.just(tvEntity)
            }

            override fun createCall(): Observable<Resource<TvEntity>> {
                val id = tvId.toString()
                return Observable.combineLatest(
                    tvApiService.fetchTvDetail(id),
                    tvApiService.fetchTvVideo(id),
                    tvApiService.fetchCastDetail(id),
                    tvApiService.fetchSimilarTvList(id, 1),
                    Function4 {
                      tvEntity: TvEntity,
                      videoResponse: VideoResponse,
                      creditResponse: CreditResponse,
                      tvApiResponse: TvApiResponse ->

                        if (videoResponse != null) {
                            tvEntity.videos = videoResponse.results
                        }

                        if (creditResponse != null) {
                            tvEntity.crews = creditResponse.crew
                            tvEntity.casts = creditResponse.cast
                        }

                        if (tvApiResponse != null) {
                            tvEntity.similarTvEntities = tvApiResponse.results
                        }
                        Resource.success(tvEntity)
                    })
            }
        }.getAsObservable()
    }


    fun searchTvs(page: Long,
                  query: String): Observable<Resource<List<TvEntity>>> {
        return object : NetworkBoundResource<List<TvEntity>, TvApiResponse>() {

            override fun saveCallResult(item: TvApiResponse) {
                val tvEntities = ArrayList<TvEntity>()
                for (tvEntity in item.results) {
                    val storedEntity = tvDao.getTvById(tvEntity.id)
                    if(storedEntity == null) {
                        tvEntity.categoryTypes = Arrays.asList(query)
                    } else {
                        val categories: MutableList<String> = mutableListOf()
                        if(storedEntity.categoryTypes != null) categories.addAll(storedEntity.categoryTypes!!)
                        categories.add(query)
                        tvEntity.categoryTypes = categories
                    }

                    tvEntity.page = item.page
                    tvEntity.totalPages = item.total_pages
                    tvEntities.add(tvEntity)
                }
                tvDao.insertTvList(tvEntities)
            }

            override fun shouldFetch(): Boolean {
                return true
            }

            override fun loadFromDb(): Flowable<List<TvEntity>> {
                val movieEntities = tvDao.getTvsByPage(page)
                return if (movieEntities == null || movieEntities.isEmpty()) {
                    Flowable.empty()
                } else Flowable.just(AppUtils.getTvsByType(query, movieEntities))
            }

            override fun createCall(): Observable<Resource<TvApiResponse>> {
                return tvApiService.searchTvsByQuery(query, "1")
                    .flatMap { tvApiResponse ->
                        Observable.just(
                            if (tvApiResponse == null)
                                Resource.error("", TvApiResponse(1, emptyList(), 0, 1))
                            else
                                Resource.success(tvApiResponse)
                        )
                    }
            }
        }.getAsObservable()
    }

}
