package com.an.trailers.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.an.trailers.data.local.entity.TvEntity;
import java.util.List;
import io.reactivex.Flowable;

@Dao
public interface TvDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertTvList(List<TvEntity> tvEntities);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateTv(TvEntity tvEntity);

    @Query("SELECT * FROM `TvEntity` where id = :id")
    Flowable<TvEntity> getTvDetailById(Long id);

    @Query("SELECT * FROM `TvEntity` where categoryType = :type")
    Flowable<List<TvEntity>> getTvListByType(String type);
}
