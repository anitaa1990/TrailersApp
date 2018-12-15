package com.an.trailers.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.an.trailers.data.local.converter.CastListTypeConverter;
import com.an.trailers.data.local.converter.CreditResponseTypeConverter;
import com.an.trailers.data.local.converter.CrewListTypeConverter;
import com.an.trailers.data.local.converter.GenreListTypeConverter;
import com.an.trailers.data.local.converter.MovieListTypeConverter;
import com.an.trailers.data.local.converter.StringListConverter;
import com.an.trailers.data.local.converter.TvListTypeConverter;
import com.an.trailers.data.local.converter.VideoListTypeConverter;
import com.an.trailers.data.local.dao.MovieDao;
import com.an.trailers.data.local.dao.TvDao;
import com.an.trailers.data.local.entity.MovieEntity;
import com.an.trailers.data.local.entity.TvEntity;

@Database(entities = {MovieEntity.class, TvEntity.class}, version = 1,  exportSchema = false)
@TypeConverters({GenreListTypeConverter.class, VideoListTypeConverter.class,
        CreditResponseTypeConverter.class, MovieListTypeConverter.class,
        CastListTypeConverter.class, CrewListTypeConverter.class,
        StringListConverter.class, TvListTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract MovieDao movieDao();

    public abstract TvDao tvDao();


    private static volatile AppDatabase INSTANCE;
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = buildDatabase(context);
                }
            }
        }
        return INSTANCE;
    }

    private static AppDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class, "Entertainment.db")
                .allowMainThreadQueries().build();
    }
}
