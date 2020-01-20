package com.levanb.movies_app.repository.datatype;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Date;

public class Movie implements Serializable {
    private int id;
    private String title;
    private String posterUrl;
    private String rawPosterUrl;
    private String backdropUrl;
    private String rawBackdropUrl;
    private String overview;
    private String originalTitle;
    private double rating;
    private Date releaseDate;

    public Movie() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setRawPosterUrl(String rawPosterUrl) {
        this.rawPosterUrl = rawPosterUrl;
    }

    public String getRawPosterUrl() {
        return rawPosterUrl;
    }

    public String getBackdropUrl() {
        return backdropUrl;
    }

    public String getRawBackdropUrl() {
        return rawBackdropUrl;
    }

    public void setBackdropUrl(String backdropUrl) {
        this.backdropUrl = backdropUrl;
    }

    public void setRawBackdropUrl(String rawBackdropUrl) {
        this.rawBackdropUrl = rawBackdropUrl;
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(this.id).hashCode();
    }

    @Override
    public boolean equals(@Nullable Object that) {
        if (!(that instanceof Movie)) {
            return false;
        }
        return this.id == ((Movie) that).id;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", overview='" + overview + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", rating=" + rating +
                ", releaseDate=" + releaseDate +
                '}';
    }
}
