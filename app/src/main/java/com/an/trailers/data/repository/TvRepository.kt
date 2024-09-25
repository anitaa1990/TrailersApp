package com.an.trailers.data.repository;


import android.support.annotation.NonNull;

import com.an.trailers.data.NetworkBoundResource;
import com.an.trailers.data.Resource;
import com.an.trailers.data.local.converter.VideoListTypeConverter;
import com.an.trailers.data.local.dao.TvDao;
import com.an.trailers.data.local.entity.TvEntity;
import com.an.trailers.data.remote.api.TvApiService;
import com.an.trailers.data.remote.model.TvApiResponse;
import com.an.trailers.utils.AppUtils;

import java.util.ArrayList;
import java.util.Arrays;
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

    public Observable<Resource<List<TvEntity>>> loadTvsByType(Long page,
                                                              String type) {
        return new NetworkBoundResource<List<TvEntity>, TvApiResponse>() {

            @Override
            protected void saveCallResult(@NonNull TvApiResponse item) {
                List<TvEntity> tvEntities = new ArrayList<>();
                for(TvEntity tvEntity : item.getResults()) {
                    TvEntity storedTvEntity = tvDao.getTvEntityById(tvEntity.getId());
                    if(storedTvEntity == null) tvEntity.setCategoryTypes(Arrays.asList(type));
                    else {
                        List<String> categories = storedTvEntity.getCategoryTypes();
                        categories.add(type);
                        tvEntity.setCategoryTypes(categories);
                    }

                    tvEntity.setPage(item.getPage());
                    tvEntity.setTotalPages(item.getTotalPages());
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

                List<TvEntity> tvEntities = tvDao.getTvListByPage(page);
                if(tvEntities == null || tvEntities.isEmpty()) {
                    return Flowable.empty();
                }
                return Flowable.just(AppUtils.getTvListByType(type, tvEntities));
            }

            @NonNull
            @Override
            protected Observable<Resource<TvApiResponse>> createCall() {
                return tvApiService.fetchTvListByType(type, page)
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
                        tvApiService.fetchTvReviews(id),
                        (tvEntity, videoResponse, creditResponse, tvApiResponse, reviewApiResponse) -> {

                            if(videoResponse != null) {
                                tvEntity.setVideos(videoResponse.getResults());
                            }

                            if(creditResponse != null) {
                                tvEntity.setCrews(creditResponse.getCrew());
                                tvEntity.setCasts(creditResponse.getCast());
                            }

                            if(tvApiResponse != null) {
                                tvEntity.setSimilarTvEntities(tvApiResponse.getResults());
                            }

                            if(reviewApiResponse != null) {
                                tvEntity.setReviews(reviewApiResponse.getResults());
                            }
                            return Resource.success(tvEntity);
                        });
            }
        }.getAsObservable();
    }


    public Observable<Resource<List<TvEntity>>> searchTvs(Long page,
                                                          String query) {
        return new NetworkBoundResource<List<TvEntity>, TvApiResponse>() {

            @Override
            protected void saveCallResult(@NonNull TvApiResponse item) {
                List<TvEntity> tvEntities = new ArrayList<>();
                for(TvEntity tvEntity : item.getResults()) {
                    TvEntity storedTvEntity = tvDao.getTvEntityById(tvEntity.getId());
                    if(storedTvEntity == null) tvEntity.setCategoryTypes(Arrays.asList(query));
                    else {
                        List<String> categories = storedTvEntity.getCategoryTypes();
                        categories.add(query);
                        tvEntity.setCategoryTypes(categories);
                    }

                    tvEntity.setPage(item.getPage());
                    tvEntity.setTotalPages(item.getTotalPages());
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
                List<TvEntity> tvEntities = tvDao.getTvListByPage(page);
                if(tvEntities == null || tvEntities.isEmpty()) {
                    return Flowable.empty();
                }
                return Flowable.just(AppUtils.getTvListByType(query, tvEntities));
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
