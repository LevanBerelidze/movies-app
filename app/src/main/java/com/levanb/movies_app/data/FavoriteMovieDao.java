package com.levanb.movies_app.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.levanb.movies_app.data.entity.FavoriteMovieEntity;

import java.util.List;

@Dao
public interface FavoriteMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(FavoriteMovieEntity movie);

    @Delete
    void removeMovieFromFavorites(FavoriteMovieEntity entity);

    @Query("SELECT * FROM favorite_movies")
    LiveData<List<FavoriteMovieEntity>> getAllMovies();
}
