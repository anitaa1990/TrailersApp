package com.an.trailers.db;


import android.support.test.runner.AndroidJUnit4;

import com.an.trailers.data.local.entity.TvEntity;
import com.an.trailers.util.MockTestUtil;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.an.trailers.AppConstants.MOVIES_POPULAR;

@RunWith(AndroidJUnit4.class)
public class TvDaoTest extends DbTest {

    @Test
    public void insertAndReadMovieTest() {
        List<TvEntity> tvEntities = new ArrayList<>();
        tvEntities.add(MockTestUtil.mockTv(MOVIES_POPULAR));

        db.tvDao().insertTvList(tvEntities);
        List<TvEntity> storedMovieEntities = db.tvDao().getTvListByType(MOVIES_POPULAR).blockingFirst();
        Assert.assertEquals(MOVIES_POPULAR, storedMovieEntities.get(0).getCategoryType());
        Assert.assertEquals(new Long(1), storedMovieEntities.get(0).getId());
    }


    @Test
    public void updateAndReadMovieTest() {
        List<TvEntity> tvEntities = new ArrayList<>();
        tvEntities.add(MockTestUtil.mockTv(MOVIES_POPULAR));

        db.tvDao().insertTvList(tvEntities);

        TvEntity storedTvEntity = db.tvDao().getTvDetailById(1l).blockingFirst();
        Assert.assertEquals(0, storedTvEntity.getCasts().size());
        Assert.assertEquals(0, storedTvEntity.getCrews().size());

        storedTvEntity.setCasts(MockTestUtil.mockCastList());
        storedTvEntity.setCrews(MockTestUtil.mockCrewList());
        storedTvEntity.setVideos(Collections.EMPTY_LIST);
        db.tvDao().updateTv(storedTvEntity);

        TvEntity updatedTvEntity = db.tvDao().getTvDetailById(1l).blockingFirst();
        Assert.assertEquals(2, updatedTvEntity.getCasts().size());
        Assert.assertEquals(2, updatedTvEntity.getCrews().size());
    }
}
