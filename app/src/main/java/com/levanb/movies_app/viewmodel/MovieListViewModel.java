package com.levanb.movies_app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.levanb.movies_app.listener.NetworkStateListener;
import com.levanb.movies_app.repository.MovieRepository;
import com.levanb.movies_app.repository.datatype.Movie;

import java.util.List;

public class MovieListViewModel extends ViewModel implements NetworkStateListener {
    private MovieRepository repository;
    private LiveData<List<Movie>> popularMovies;
    private LiveData<List<Movie>> topRatedMovies;
    private LiveData<List<Movie>> favoriteMovies;
    private MutableLiveData<Boolean> isConnectedToNetwork;

    public MovieListViewModel(MovieRepository repository) {
        this.repository = repository;
        this.popularMovies = repository.getPopularMovies();
        this.topRatedMovies = repository.getTopRatedMovies();
        this.favoriteMovies = repository.getFavoriteMovies();
        isConnectedToNetwork = new MutableLiveData<>();
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

    public LiveData<Boolean> isConnectedToNetwork() {
        return isConnectedToNetwork;
    }

    public void setFavorite(Movie movie, boolean isFavorite) {
        repository.setFavorite(movie, isFavorite);
    }

    public LiveData<Movie> getFavoriteMovieById(int id) {
        return repository.getFavoriteMovieById(id);
    }

    @Override
    public void onNetworkStateChanged(boolean isConnected) {
        isConnectedToNetwork.postValue(isConnected);
    }
}
