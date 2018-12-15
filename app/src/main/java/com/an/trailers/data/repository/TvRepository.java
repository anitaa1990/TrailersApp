package com.an.trailers.data.repository;


import android.support.annotation.NonNull;

import com.an.trailers.data.NetworkBoundResource;
import com.an.trailers.data.Resource;
import com.an.trailers.data.local.converter.VideoListTypeConverter;
import com.an.trailers.data.local.dao.TvDao;
import com.an.trailers.data.local.entity.TvEntity;
import com.an.trailers.data.remote.api.TvApiService;
import com.an.trailers.data.remote.model.TvApiResponse;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Singleton;
import io.reactivex.Flowable;
import io.reactivex.Observable;

@Singleton
public class TvRepository {

    private TvDao tvDao;
    private TvApiService tvApiService;
    public TvRepository(TvDao tvDao,
                        TvApiService tvApiService) {
        this.tvDao = tvDao;
        this.tvApiService = tvApiService;
    }

    public Observable<Resource<List<TvEntity>>> loadTvsByType(String type) {
        return new NetworkBoundResource<List<TvEntity>, TvApiResponse>() {

            @Override
            protected void saveCallResult(@NonNull TvApiResponse item) {
                List<TvEntity> tvEntities = new ArrayList<>();
                for(TvEntity tvEntity : item.getResults()) {
                    tvEntity.setCategoryType(type);
                    tvEntities.add(tvEntity);
                }
                tvDao.insertTvList(tvEntities);
            }

            @Override
            protected boolean shouldFetch() {
                return true;
            }

            @NonNull
            @Override
            protected Flowable<List<TvEntity>> loadFromDb() {
                return tvDao.getTvListByType(type);
            }

            @NonNull
            @Override
            protected Observable<Resource<TvApiResponse>> createCall() {
                return tvApiService.fetchTvListByType(type, 1)
                        .flatMap(tvApiResponse -> Observable.just(tvApiResponse == null
                                ? Resource.error("", new TvApiResponse())
                                : Resource.success(tvApiResponse)));
            }
        }.getAsObservable();
    }


    public Observable<Resource<TvEntity>> fetchTvDetails(Long tvId) {
        return new NetworkBoundResource<TvEntity, TvEntity>() {
            @Override
            protected void saveCallResult(@NonNull TvEntity item) {
                TvEntity tvEntity = tvDao.getTvEntityById(item.getId());
                if(tvEntity == null) tvDao.insertTv(item);
                else tvDao.updateTv(item);
            }

            @Override
            protected boolean shouldFetch() {
                return true;
            }

            @NonNull
            @Override
            protected Flowable<TvEntity> loadFromDb() {
                TvEntity tvEntity = tvDao.getTvEntityById(tvId);
                if(tvEntity == null) return Flowable.empty();
                return Flowable.just(tvEntity);
            }

            @NonNull
            @Override
            protected Observable<Resource<TvEntity>> createCall() {
                String id = String.valueOf(tvId);
                return Observable.combineLatest(tvApiService.fetchTvDetail(id),
                        tvApiService.fetchTvVideo(id),
                        tvApiService.fetchCastDetail(id),
                        tvApiService.fetchSimilarTvList(id, 1),
                        (tvEntity, videoResponse, creditResponse, tvApiResponse) -> {

                            if(videoResponse != null) {
                                tvEntity.setVideos(new VideoListTypeConverter()
                                        .fromVideos(videoResponse.getResults()));
                            }

                            if(creditResponse != null) {
                                tvEntity.setCrews(creditResponse.getCrew());
                                tvEntity.setCasts(creditResponse.getCast());
                            }

                            if(tvApiResponse != null) {
                                tvEntity.setSimilarTvEntities(tvApiResponse.getResults());
                            }
                            return Resource.success(tvEntity);
                        });
            }
        }.getAsObservable();
    }


    public Observable<Resource<List<TvEntity>>> searchTvs(String query) {
        return new NetworkBoundResource<List<TvEntity>, TvApiResponse>() {

            @Override
            protected void saveCallResult(@NonNull TvApiResponse item) {
                List<TvEntity> tvEntities = new ArrayList<>();
                for(TvEntity tvEntity : item.getResults()) {
                    tvEntity.setCategoryType(query);
                    tvEntities.add(tvEntity);
                }
                tvDao.insertTvList(tvEntities);
            }

            @Override
            protected boolean shouldFetch() {
                return true;
            }

            @NonNull
            @Override
            protected Flowable<List<TvEntity>> loadFromDb() {
                return tvDao.getTvListByType(query);
            }

            @NonNull
            @Override
            protected Observable<Resource<TvApiResponse>> createCall() {
                return tvApiService.searchTvsByQuery(query, "1")
                        .flatMap(tvApiResponse -> Observable.just(tvApiResponse == null
                                ? Resource.error("", new TvApiResponse())
                                : Resource.success(tvApiResponse)));
            }
        }.getAsObservable();
    }

}
