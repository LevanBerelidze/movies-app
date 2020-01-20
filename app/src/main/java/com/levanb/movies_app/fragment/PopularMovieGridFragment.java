package com.levanb.movies_app.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashSet;

public class PopularMovieGridFragment extends BaseMovieGridFragment {
    @Override
    public void onViewCreated(@NonNull View contentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(contentView, savedInstanceState);

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
    }
}
