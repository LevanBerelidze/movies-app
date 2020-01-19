package com.levanb.movies_app.repository;

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
    private static MovieRepository instance;

    private MovieDatabase database;
    private MovieService service;
    private MutableLiveData<List<Movie>> popularMovies;
    private MutableLiveData<List<Movie>> topRatedMovies;
    private LiveData<List<Movie>> favoriteMovies;

    public MovieRepository(MovieDatabase database, MovieService service) {
        this.database = database;
        this.service = service;
        popularMovies = new MutableLiveData<>();
        topRatedMovies = new MutableLiveData<>();
        favoriteMovies = new MutableLiveData<>();

        initialize();
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
        // TODO asynchronously
        if (isFavorite) {
            database.favoriteMovieDao().insertMovie(transform(movie));
        } else {
            database.favoriteMovieDao().removeMovieFromFavorites(transform(movie));
        }
    }

    private void initialize() {
        // load popular rated
        Call<MovieResponse> popularMoviesCall = service.getPopularMovies(API_KEY, 1);
        popularMoviesCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                List<MovieSchema> movies = response.body().getResults();
                popularMovies.postValue(Mapper.transformSchemas(movies));
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });

        // load top rated
        Call<MovieResponse> topRatedMoviesCall = service.getTopRatedMovies(API_KEY, 1);
        topRatedMoviesCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                List<MovieSchema> movies = response.body().getResults();
                topRatedMovies.postValue(Mapper.transformSchemas(movies));
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });

        // load favorite movies
        favoriteMovies = Transformations.map(
                database.favoriteMovieDao().getAllMovies(),
                Mapper::transformEntities
        );
    }
}
