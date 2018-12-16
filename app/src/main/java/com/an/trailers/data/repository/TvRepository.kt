package com.an.trailers.data.repository


import com.an.trailers.data.NetworkBoundResource
import com.an.trailers.data.Resource
import com.an.trailers.data.local.converter.VideoListTypeConverter
import com.an.trailers.data.local.dao.TvDao
import com.an.trailers.data.local.entity.TvEntity
import com.an.trailers.data.remote.api.TvApiService
import com.an.trailers.data.remote.model.CreditResponse
import com.an.trailers.data.remote.model.TvApiResponse
import com.an.trailers.data.remote.model.VideoResponse
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.functions.Function4

import javax.inject.Singleton
import java.util.ArrayList

@Singleton
class TvRepository(
    private val tvDao: TvDao,
    private val tvApiService: TvApiService
) {

    fun loadTvsByType(type: String): Observable<Resource<List<TvEntity>>> {
        return object : NetworkBoundResource<List<TvEntity>, TvApiResponse>() {

            override fun saveCallResult(item: TvApiResponse) {
                val tvEntities = ArrayList<TvEntity>()
                for (tvEntity in item.results) {
                    tvEntity.categoryType = type
                    tvEntities.add(tvEntity)
                }
                tvDao.insertTvList(tvEntities)
            }

            override fun shouldFetch(): Boolean {
                return true
            }

            override fun loadFromDb(): Flowable<List<TvEntity>> {
                return tvDao.getTvListByType(type)
            }

            override fun createCall(): Observable<Resource<TvApiResponse>> {
                return tvApiService.fetchTvListByType(type, 1)
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


    fun fetchTvDetails(tvId: Long?): Observable<Resource<TvEntity>> {
        return object : NetworkBoundResource<TvEntity, TvEntity>() {
            override fun saveCallResult(item: TvEntity) {
                val tvEntity: TvEntity = tvDao.getTvById(tvId)
                if(null == tvEntity) tvDao.insertTv(item)
                else tvDao.updateTv(item)
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


    fun searchTvs(query: String): Observable<Resource<List<TvEntity>>> {
        return object : NetworkBoundResource<List<TvEntity>, TvApiResponse>() {

            override fun saveCallResult(item: TvApiResponse) {
                val tvEntities = ArrayList<TvEntity>()
                for (tvEntity in item.results) {
                    tvEntity.categoryType = query
                    tvEntities.add(tvEntity)
                }
                tvDao.insertTvList(tvEntities)
            }

            override fun shouldFetch(): Boolean {
                return true
            }

            override fun loadFromDb(): Flowable<List<TvEntity>> {
                return tvDao.getTvListByType(query)
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
