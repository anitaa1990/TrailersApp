package com.an.trailers.api;

import com.an.trailers.AppConstants;
import com.an.trailers.data.local.entity.MovieEntity;
import com.an.trailers.data.remote.api.MovieApiService;
import com.an.trailers.data.remote.model.CreditResponse;
import com.an.trailers.data.remote.model.MovieApiResponse;
import com.an.trailers.data.remote.model.VideoResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class MovieApiServiceTest extends ApiAbstract<MovieApiService> {

    private MovieApiService service;

    @Before
    public void initService() {
        this.service = createService(MovieApiService.class);
    }

    @Test
    public void fetchMovieDetailsTest() throws IOException {
        enqueueResponse("test_movie_details.json");
        MovieEntity movieEntity = service.fetchMovieDetail("1").blockingFirst();
        Assert.assertEquals("Venom", movieEntity.getHeader());
        Assert.assertEquals(String.format(AppConstants.IMAGE_URL, "/2uNW4WbgBXL25BAbXGLnLqX71Sw.jpg"), movieEntity.getPosterPath());
    }


    @Test
    public void fetchMovieVideosTest() throws IOException {
        enqueueResponse("test_movie_videos.json");
        VideoResponse videoResponse = service.fetchMovieVideo("1").blockingFirst();
        Assert.assertEquals(335983, videoResponse.getId());
        Assert.assertEquals(4, videoResponse.getResults().size());
        Assert.assertEquals("dzxFdtWmjto", videoResponse.getResults().get(0).getKey());
    }


    @Test
    public void fetchMovieCastsTest() throws IOException {
        enqueueResponse("test_movie_cast.json");
        CreditResponse creditResponse = service.fetchCastDetail("1").blockingFirst();
        Assert.assertEquals(102, creditResponse.getCast().size());
        Assert.assertEquals(48, creditResponse.getCrew().size());
    }


    @Test
    public void fetchMovieSimilarTest() throws IOException {
        enqueueResponse("test_movie_similar.json");
        MovieApiResponse movieApiResponse = service.fetchSimilarMovie("1", 1).blockingFirst();
        Assert.assertEquals(1, movieApiResponse.getPage());
        Assert.assertEquals(20, movieApiResponse.getResults().size());
    }


    @Test
    public void searchMoviesTest() throws IOException {
        enqueueResponse("test_search_movie.json");
        MovieApiResponse movieApiResponse = service.searchMoviesByQuery("thor", "1").blockingFirst();
        Assert.assertEquals(1, movieApiResponse.getPage());
        Assert.assertEquals(20, movieApiResponse.getResults().size());
        Assert.assertEquals("Thor", movieApiResponse.getResults().get(0).getHeader());
    }
}
