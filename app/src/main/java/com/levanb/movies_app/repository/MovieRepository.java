package com.levanb.movies_app.repository;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.levanb.movies_app.api.MovieResponse;
import com.levanb.movies_app.api.MovieSchema;
import com.levanb.movies_app.api.MovieService;
import com.levanb.movies_app.data.MovieDatabase;
import com.levanb.movies_app.repository.datatype.Mapper;
import com.levanb.movies_app.repository.datatype.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.levanb.movies_app.api.MovieService.API_KEY;
import static com.levanb.movies_app.repository.datatype.Mapper.transform;

public class MovieRepository {
    private MovieDatabase database;
    private MovieService service;
    private MutableLiveData<List<Movie>> popularMovies;
    private MutableLiveData<List<Movie>> topRatedMovies;
    private LiveData<List<Movie>> favoriteMovies;

    public MovieRepository(MovieDatabase database, MovieService service) {
        this.database = database;
        this.service = service;
    }

    public LiveData<List<Movie>> getPopularMovies() {
        if(popularMovies != null) {
            return popularMovies;
        }

        popularMovies = new MutableLiveData<>();

        Call<MovieResponse> popularMoviesCall = service.getPopularMovies(API_KEY, 1);
        popularMoviesCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                List<MovieSchema> movies = response.body().getResults();
                popularMovies.postValue(Mapper.transformSchemas(movies));
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                // TODO
            }
        });

        return popularMovies;
    }

    public LiveData<List<Movie>> getTopRatedMovies() {
        if (topRatedMovies != null) {
            return topRatedMovies;
        }

        topRatedMovies = new MutableLiveData<>();

        Call<MovieResponse> topRatedMoviesCall = service.getTopRatedMovies(API_KEY, 1);
        topRatedMoviesCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                List<MovieSchema> movies = response.body().getResults();
                topRatedMovies.postValue(Mapper.transformSchemas(movies));
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                // TODO
            }
        });

        return topRatedMovies;
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
