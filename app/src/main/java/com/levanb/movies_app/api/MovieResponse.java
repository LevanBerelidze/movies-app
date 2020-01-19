package com.levanb.movies_app.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponse {
    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<MovieSchema> results;

    @SerializedName("total_results")
    private int numTotalMovies;

    @SerializedName("total_pages")
    private int numTotalPages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MovieSchema> getResults() {
        return results;
    }

    public void setResults(List<MovieSchema> results) {
        this.results = results;
    }

    public int getNumTotalMovies() {
        return numTotalMovies;
    }

    public void setNumTotalMovies(int numTotalMovies) {
        this.numTotalMovies = numTotalMovies;
    }

    public int getNumTotalPages() {
        return numTotalPages;
    }

    public void setNumTotalPages(int numTotalPages) {
        this.numTotalPages = numTotalPages;
    }
}
