package com.levanb.movies_app.data;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.levanb.movies_app.data.entity.FavoriteMovieEntity;
import com.levanb.movies_app.data.util.Converters;

@androidx.room.Database(entities = {FavoriteMovieEntity.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MovieDatabase extends RoomDatabase {
    private static final String DB_NAME = "movie_db";
    public static MovieDatabase instance;

    public abstract FavoriteMovieDao favoriteMovieDao();

    public static MovieDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (MovieDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, MovieDatabase.class, DB_NAME).build();
                }
            }
        }
        return instance;
    }
}
