package com.levanb.movies_app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashSet;

public class PopularMovieGridFragment extends BaseMovieGridFragment {
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View contentView = super.onCreateView(inflater, container, savedInstanceState);
        super.viewModel.getPopularMovies().observe(this, movies -> {
            adapter.setItems(movies);
            adapter.notifyDataSetChanged();
        });
        super.viewModel.getFavoriteMovies().observe(this, movies -> {
            Iterable<Integer> modifiedIndices = adapter.setFavorites(new HashSet<>(movies));
            for (int i : modifiedIndices) {
                adapter.notifyItemChanged(i);
            }
        });
        return contentView;
    }
}
