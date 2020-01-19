package com.levanb.movies_app.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "favorite_movies")
public class FavoriteMovieEntity {
    @PrimaryKey
    public int id;

    public String title;

    public String posterUrl;

    public String overview;

    public String originalTitle;

    public double rating;

    public Date releaseDate;
}
