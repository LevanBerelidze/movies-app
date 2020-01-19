package com.levanb.movies_app.listener;

import com.levanb.movies_app.repository.datatype.Movie;

public interface MovieFavoriteStatusListener {
    void onMovieFavoriteStatusChanged(Movie movie, boolean isFavorite);
}
