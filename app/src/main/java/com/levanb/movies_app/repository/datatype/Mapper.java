package com.levanb.movies_app.repository.datatype;

import android.util.Log;

import com.levanb.movies_app.api.MovieSchema;
import com.levanb.movies_app.data.entity.FavoriteMovieEntity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import static com.levanb.movies_app.api.MovieService.API_KEY;
import static com.levanb.movies_app.api.MovieService.POSTER_BASE_URL;

public class Mapper {
    private static DateFormat apiDateFormat;

    static {
        apiDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public static Movie transform(MovieSchema movieSchema) {
        Movie movie = new Movie();
        movie.setId(movieSchema.getId());
        movie.setOriginalTitle(movieSchema.getOriginalTitle());
        movie.setOverview(movieSchema.getOverview());
        movie.setPosterUrl(getFullImageURL(movieSchema.getPosterPath()));
        movie.setRawPosterUrl(movieSchema.getPosterPath());
        movie.setRating(movieSchema.getRating());
        movie.setTitle(movieSchema.getTitle());
        movie.setReleaseDate(parseDate(movieSchema.getReleaseDate()));
        movie.setRawBackdropUrl(movieSchema.getBackdropPath());
        movie.setBackdropUrl(getFullImageURL(movieSchema.getBackdropPath()));
        return movie;
    }

    public static Movie transform(FavoriteMovieEntity entity) {
        Movie movie = new Movie();
        movie.setId(entity.id);
        movie.setOriginalTitle(entity.originalTitle);
        movie.setOverview(entity.overview);
        movie.setPosterUrl(getFullImageURL(entity.posterUrl));
        movie.setRawPosterUrl(entity.posterUrl);
        movie.setRating(entity.rating);
        movie.setTitle(entity.title);
        movie.setReleaseDate(entity.releaseDate);
        movie.setRawBackdropUrl(entity.backdropUrl);
        movie.setBackdropUrl(getFullImageURL(entity.backdropUrl));
        return movie;
    }

    public static FavoriteMovieEntity transform(Movie movie) {
        FavoriteMovieEntity entity = new FavoriteMovieEntity();
        entity.id = movie.getId();
        entity.originalTitle = movie.getOriginalTitle();
        entity.overview = movie.getOverview();
        entity.posterUrl = movie.getRawPosterUrl();
        entity.rating = movie.getRating();
        entity.title = movie.getTitle();
        entity.releaseDate = movie.getReleaseDate();
        entity.backdropUrl = movie.getRawBackdropUrl();
        return entity;
    }

    public static List<Movie> transformSchemas(List<MovieSchema> movieSchemas) {
        List<Movie> result = new ArrayList<>();
        for (MovieSchema schema : movieSchemas) {
            result.add(transform(schema));
        }
        return result;
    }

    public static List<Movie> transformEntities(List<FavoriteMovieEntity> entities) {
        List<Movie> result = new ArrayList<>();
        for (FavoriteMovieEntity entity : entities) {
            result.add(transform(entity));
        }
        return result;
    }

    private static Date parseDate(String date) {
        try {
            return apiDateFormat.parse(date);
        } catch (ParseException e) {
            Log.e("Date", String.format("error parsing date: %s", date));
        }
        return null;
    }

    private static String getFullImageURL(String posterUrl) {
        return posterUrl != null ?
                String.format("%s%s?api_key=%s", POSTER_BASE_URL, posterUrl.substring(1), API_KEY) :
                null;
    }
}
