package com.levanb.movies_app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.levanb.movies_app.repository.MovieRepository;
import com.levanb.movies_app.repository.datatype.Movie;

import java.util.List;

public class MovieListViewModel extends ViewModel {
    private MovieRepository repository;
    private LiveData<List<Movie>> popularMovies;
    private LiveData<List<Movie>> topRatedMovies;
    private LiveData<List<Movie>> favoriteMovies;

    public MovieListViewModel(MovieRepository repository) {
        this.repository = repository;
        this.popularMovies = repository.getPopularMovies();
        this.topRatedMovies = repository.getTopRatedMovies();
        this.favoriteMovies = repository.getFavoriteMovies();
    }

    public LiveData<List<Movie>> getPopularMovies() {
        return popularMovies;
    }

    public LiveData<List<Movie>> getTopRatedMovies() {
        return topRatedMovies;
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return favoriteMovies;
    }

    public void setFavorite(Movie movie, boolean isFavorite) {
        repository.setFavorite(movie, isFavorite);
    }
}
