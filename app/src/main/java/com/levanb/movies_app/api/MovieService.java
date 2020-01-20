package com.levanb.movies_app.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {
    String API_KEY = "98e244646b1471544fa75c527195723a";
    String BASE_URL = "https://api.themoviedb.org/3/";
    String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w400/";

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
        @Query("api_key") String apiKey,
        @Query("page") int page
    );

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(
        @Query("api_key") String apiKey,
        @Query("page") int page
    );
}
