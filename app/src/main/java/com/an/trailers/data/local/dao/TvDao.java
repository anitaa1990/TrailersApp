package com.an.trailers.data.local.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.an.trailers.data.local.entity.TvEntity;
import java.util.List;
import io.reactivex.Flowable;

@Dao
public interface TvDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertTvList(List<TvEntity> tvEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTv(TvEntity tvEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateTv(TvEntity tvEntity);

    @Query("SELECT * FROM `TvEntity` where id = :id")
    TvEntity getTvEntityById(Long id);

    @Query("SELECT * FROM `TvEntity` where id = :id")
    Flowable<TvEntity> getTvDetailById(Long id);

    @Query("SELECT * FROM `TvEntity` where page = :page")
    List<TvEntity> getTvListByPage(Long page);
}
