package com.an.trailers.data.local.dao

import android.arch.persistence.room.*
import com.an.trailers.data.local.entity.TvEntity
import io.reactivex.Flowable

@Dao
interface TvDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTvList(tvEntities: List<TvEntity>): LongArray

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTv(tvEntity: TvEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTv(tvEntity: TvEntity): Int

    @Query("SELECT * FROM `TvEntity` where id = :id")
    fun getTvById(id: Long?): TvEntity

    @Query("SELECT * FROM `TvEntity` where id = :id")
    fun getTvDetailById(id: Long?): Flowable<TvEntity>

    @Query("SELECT * FROM `TvEntity` where page = :page")
    fun getTvsByPage(page: Long): List<TvEntity>
}
