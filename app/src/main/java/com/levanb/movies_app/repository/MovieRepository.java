package com.levanb.movies_app.repository;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.levanb.movies_app.api.MovieResponse;
import com.levanb.movies_app.api.MovieSchema;
import com.levanb.movies_app.api.MovieService;
import com.levanb.movies_app.data.MovieDatabase;
import com.levanb.movies_app.listener.NetworkStateListener;
import com.levanb.movies_app.repository.datatype.Mapper;
import com.levanb.movies_app.repository.datatype.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.levanb.movies_app.api.MovieService.API_KEY;
import static com.levanb.movies_app.repository.datatype.Mapper.transform;

public class MovieRepository implements NetworkStateListener {
    private MovieDatabase database;
    private MovieService service;
    private MutableLiveData<List<Movie>> popularMovies;
    private MutableLiveData<List<Movie>> topRatedMovies;
    private LiveData<List<Movie>> favoriteMovies;
    private boolean isConnected;

    public MovieRepository(MovieDatabase database, MovieService service) {
        this.database = database;
        this.service = service;
    }

    @Override
    public void onNetworkStateChanged(boolean isConnected) {
        if (!this.isConnected && isConnected) {
            // lazily load data
            if (popularMovies != null) {
                loadPopularMovies();
            }
            if (topRatedMovies != null) {
                loadTopRatedMovies();
            }
        }
        this.isConnected = isConnected;
    }

    public LiveData<List<Movie>> getPopularMovies() {
        if(popularMovies != null) {
            return popularMovies;
        }

        popularMovies = new MutableLiveData<>();
        loadPopularMovies();
        return popularMovies;
    }

    private void loadPopularMovies() {
        Call<MovieResponse> popularMoviesCall = service.getPopularMovies(API_KEY, 1);
        popularMoviesCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse body = response.body();
                if (body != null) {
                    List<MovieSchema> movies = body.getResults();
                    popularMovies.postValue(Mapper.transformSchemas(movies));
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                // TODO
            }
        });
    }

    public LiveData<List<Movie>> getTopRatedMovies() {
        if (topRatedMovies != null) {
            return topRatedMovies;
        }

        topRatedMovies = new MutableLiveData<>();
        loadTopRatedMovies();
        return topRatedMovies;
    }

    private void loadTopRatedMovies() {
        Call<MovieResponse> topRatedMoviesCall = service.getTopRatedMovies(API_KEY, 1);
        topRatedMoviesCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse body = response.body();
                if (body != null) {
                    List<MovieSchema> movies = body.getResults();
                    topRatedMovies.postValue(Mapper.transformSchemas(movies));
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                // TODO
            }
        });
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        if (favoriteMovies != null) {
            return favoriteMovies;
        }

        favoriteMovies = new MutableLiveData<>();
        favoriteMovies = Transformations.map(
                database.favoriteMovieDao().getAllMovies(),
                Mapper::transformEntities
        );
        return favoriteMovies;
    }

    public LiveData<Movie> getFavoriteMovieById(int id) {
        return Transformations.map(database.favoriteMovieDao().getFavoriteMovieById(id), Mapper::transform);
    }

    public void setFavorite(Movie movie, boolean isFavorite) {
        AsyncTask.execute(() -> {
            if (isFavorite) {
                database.favoriteMovieDao().insertMovie(transform(movie));
            } else {
                database.favoriteMovieDao().removeMovieFromFavorites(transform(movie));
            }
        });
    }
}
