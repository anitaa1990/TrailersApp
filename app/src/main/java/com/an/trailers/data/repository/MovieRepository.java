package com.an.trailers.data.repository;


import android.support.annotation.NonNull;
import com.an.trailers.data.NetworkBoundResource;

import com.an.trailers.data.Resource;
import com.an.trailers.data.local.converter.VideoListTypeConverter;
import com.an.trailers.data.local.dao.MovieDao;
import com.an.trailers.data.local.entity.MovieEntity;
import com.an.trailers.data.remote.api.MovieApiService;
import com.an.trailers.data.remote.model.MovieApiResponse;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Singleton;
import io.reactivex.Flowable;
import io.reactivex.Observable;

@Singleton
public class MovieRepository {

    private MovieDao movieDao;
    private MovieApiService movieApiService;
    public MovieRepository(MovieDao movieDao,
                           MovieApiService movieApiService) {
        this.movieDao = movieDao;
        this.movieApiService = movieApiService;
    }

    public Observable<Resource<List<MovieEntity>>> loadMoviesByType(String type) {
        return new NetworkBoundResource<List<MovieEntity>, MovieApiResponse>() {

            @Override
            protected void saveCallResult(@NonNull MovieApiResponse item) {
                List<MovieEntity> movieEntities = new ArrayList<>();
                for(MovieEntity movieEntity : item.getResults()) {
                    movieEntity.setCategoryType(type);
                    movieEntities.add(movieEntity);
                }
                movieDao.insertMovies(movieEntities);
            }

            @Override
            protected boolean shouldFetch() {
                return true;
            }

            @NonNull
            @Override
            protected Flowable<List<MovieEntity>> loadFromDb() {
                return movieDao.getMoviesByType(type);
            }

            @NonNull
            @Override
            protected Observable<Resource<MovieApiResponse>> createCall() {
                return movieApiService.fetchMoviesByType(type, 1)
                        .flatMap(movieApiResponse -> Observable.just(movieApiResponse == null
                                ? Resource.error("", new MovieApiResponse())
                                : Resource.success(movieApiResponse)));
            }
        }.getAsObservable();
    }


    public Observable<Resource<MovieEntity>> fetchMovieDetails(Long movieId) {
        return new NetworkBoundResource<MovieEntity, MovieEntity>() {
            @Override
            protected void saveCallResult(@NonNull MovieEntity item) {
                MovieEntity movieEntity = movieDao.getMovieById(item.getId());
                if(movieEntity == null) movieDao.insertMovie(item);
                else movieDao.updateMovie(item);
            }

            @Override
            protected boolean shouldFetch() {
                return true;
            }

            @NonNull
            @Override
            protected Flowable<MovieEntity> loadFromDb() {
                MovieEntity movieEntity = movieDao.getMovieById(movieId);
                if(movieEntity == null) return Flowable.empty();
                return Flowable.just(movieEntity);
            }

            @NonNull
            @Override
            protected Observable<Resource<MovieEntity>> createCall() {
                String id = String.valueOf(movieId);
                return Observable.combineLatest(movieApiService.fetchMovieDetail(id),
                        movieApiService.fetchMovieVideo(id),
                        movieApiService.fetchCastDetail(id),
                        movieApiService.fetchSimilarMovie(id, 1),
                        (movieEntity, videoResponse, creditResponse, movieApiResponse) -> {

                            if(videoResponse != null) {
                                movieEntity.setVideos(new VideoListTypeConverter()
                                        .fromVideos(videoResponse.getResults()));
                            }

                            if(creditResponse != null) {
                                movieEntity.setCrews(creditResponse.getCrew());
                                movieEntity.setCasts(creditResponse.getCast());
                            }

                            if(movieApiResponse != null) {
                                movieEntity.setSimilarMovies(movieApiResponse.getResults());
                            }
                            return Resource.success(movieEntity);
                        });
            }
        }.getAsObservable();
    }


    public Observable<Resource<List<MovieEntity>>> searchMovies(String query) {
        return new NetworkBoundResource<List<MovieEntity>, MovieApiResponse>() {

            @Override
            protected void saveCallResult(@NonNull MovieApiResponse item) {
                List<MovieEntity> movieEntities = new ArrayList<>();
                for(MovieEntity movieEntity : item.getResults()) {
                    movieEntity.setCategoryType(query);
                    movieEntities.add(movieEntity);
                }
                movieDao.insertMovies(movieEntities);
            }

            @Override
            protected boolean shouldFetch() {
                return true;
            }

            @NonNull
            @Override
            protected Flowable<List<MovieEntity>> loadFromDb() {
                return movieDao.getMoviesByType(query);
            }

            @NonNull
            @Override
            protected Observable<Resource<MovieApiResponse>> createCall() {
                return movieApiService.searchMoviesByQuery(query, "1")
                        .flatMap(movieApiResponse -> Observable.just(movieApiResponse == null
                                ? Resource.error("", new MovieApiResponse())
                                : Resource.success(movieApiResponse)));
            }
        }.getAsObservable();
    }

}
