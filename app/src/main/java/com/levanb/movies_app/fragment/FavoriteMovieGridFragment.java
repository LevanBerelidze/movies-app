package com.levanb.movies_app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.levanb.movies_app.repository.datatype.Movie;

import java.util.HashSet;
import java.util.List;

public class FavoriteMovieGridFragment extends BaseMovieGridFragment {
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View contentView = super.onCreateView(inflater, container, savedInstanceState);
        LiveData<List<Movie>> moviesLiveData = super.viewModel.getFavoriteMovies();
        moviesLiveData.observe(this, movies -> {
            adapter.setFavorites(new HashSet<>(movies));
            adapter.setItems(movies);
            adapter.notifyDataSetChanged();
        });
        return contentView;
    }
}
