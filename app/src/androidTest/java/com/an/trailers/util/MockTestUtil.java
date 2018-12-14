package com.an.trailers.util;

import com.an.trailers.data.local.entity.MovieEntity;
import com.an.trailers.data.local.entity.TvEntity;
import com.an.trailers.data.remote.model.Cast;
import com.an.trailers.data.remote.model.Crew;
import com.an.trailers.data.remote.model.MovieApiResponse;
import com.an.trailers.data.remote.model.TvApiResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MockTestUtil {


    public static MovieApiResponse mockMovieApiResponse(String type) {
        MovieApiResponse movieApiResponse = new MovieApiResponse();
        movieApiResponse.setResults(mockMovieList(type));
        return movieApiResponse;
    }

    public static TvApiResponse mockTvApiResponse(String type) {
        TvApiResponse tvApiResponse = new TvApiResponse();
        tvApiResponse.setResults(mockTvList(type));
        return tvApiResponse;
    }

    public static List<MovieEntity> mockMovieList(String type) {
        List<MovieEntity> movieEntities = new ArrayList<>();

        MovieEntity movieEntity1 = new MovieEntity();
        movieEntity1.setCategoryType(type);
        movieEntities.add(movieEntity1);

        MovieEntity movieEntity2 = new MovieEntity();
        movieEntity2.setCategoryType(type);
        movieEntities.add(movieEntity2);

        return movieEntities;
    }


    public static List<TvEntity> mockTvList(String type) {
        List<TvEntity> tvEntities = new ArrayList<>();

        TvEntity tvEntity1 = new TvEntity();
        tvEntity1.setCategoryType(type);
        tvEntities.add(tvEntity1);

        TvEntity tvEntity2 = new TvEntity();
        tvEntity2.setCategoryType(type);
        tvEntities.add(tvEntity2);

        return tvEntities;
    }

    public static MovieEntity mockMovie(String type) {
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setId(1l);
        movieEntity.setCategoryType(type);
        return movieEntity;
    }


    public static MovieEntity mockMovieDetail(String type) {
        MovieEntity movieEntity = mockMovie(type);
        movieEntity.setCrews(mockCrewList());
        movieEntity.setCasts(mockCastList());
        return movieEntity;
    }


    public static TvEntity mockTvDetail(String type) {
        TvEntity tvEntity = mockTv(type);
        tvEntity.setCrews(mockCrewList());
        tvEntity.setCasts(mockCastList());
        return tvEntity;
    }


    public static TvEntity mockTv(String type) {
        TvEntity tvEntity = new TvEntity();
        tvEntity.setId(1l);
        tvEntity.setCategoryType(type);
        return tvEntity;
    }

    public static List<Crew> mockCrewList() {
        List<Crew> crews = new ArrayList<>();
        crews.add(new Crew());
        crews.add(new Crew());
        return crews;
    }


    public static List<Cast> mockCastList() {
        List<Cast> casts = new ArrayList<>();
        casts.add(new Cast());
        casts.add(new Cast());
        return casts;
    }
}