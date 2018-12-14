package com.an.trailers.db;

import android.support.test.runner.AndroidJUnit4;

import com.an.trailers.data.local.entity.MovieEntity;
import com.an.trailers.util.MockTestUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.an.trailers.AppConstants.MOVIES_POPULAR;

@RunWith(AndroidJUnit4.class)
public class MovieDaoTest extends DbTest {

    @Test
    public void insertAndReadMovieTest() {
        List<MovieEntity> movieEntities = new ArrayList<>();
        movieEntities.add(MockTestUtil.mockMovie(MOVIES_POPULAR));

        db.movieDao().insertMovies(movieEntities);
        List<MovieEntity> storedMovieEntities = db.movieDao().getMoviesByType(MOVIES_POPULAR).blockingFirst();
        Assert.assertEquals(MOVIES_POPULAR, storedMovieEntities.get(0).getCategoryType());
        Assert.assertEquals(new Long(1), storedMovieEntities.get(0).getId());
    }


    @Test
    public void updateAndReadMovieTest() {
        List<MovieEntity> movieEntities = new ArrayList<>();
        movieEntities.add(MockTestUtil.mockMovie(MOVIES_POPULAR));

        db.movieDao().insertMovies(movieEntities);

        MovieEntity storedMovieEntity = db.movieDao().getMovieDetailById(1l).blockingFirst();
        Assert.assertEquals(0, storedMovieEntity.getCasts().size());
        Assert.assertEquals(0, storedMovieEntity.getCrews().size());

        storedMovieEntity.setCasts(MockTestUtil.mockCastList());
        storedMovieEntity.setCrews(MockTestUtil.mockCrewList());
        db.movieDao().updateMovie(storedMovieEntity);

        MovieEntity updatedMovieEntity = db.movieDao().getMovieDetailById(1l).blockingFirst();
        Assert.assertEquals(2, updatedMovieEntity.getCasts().size());
        Assert.assertEquals(2, updatedMovieEntity.getCrews().size());
    }
}
