package com.levanb.movies_app.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.levanb.movies_app.repository.MovieRepository;

public class MovieListViewModelFactory implements ViewModelProvider.Factory {
    private MovieRepository repository;

    public MovieListViewModelFactory(MovieRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieListViewModel(repository);
    }
}
