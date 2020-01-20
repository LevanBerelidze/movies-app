package com.levanb.movies_app.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.levanb.movies_app.repository.datatype.Movie;

import java.util.HashSet;
import java.util.List;

public class FavoriteMovieGridFragment extends BaseMovieGridFragment {
    @Override
    public void onViewCreated(@NonNull View contentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(contentView, savedInstanceState);

        LiveData<List<Movie>> moviesLiveData = super.viewModel.getFavoriteMovies();
        moviesLiveData.observe(this, movies -> {
            adapter.setFavorites(new HashSet<>(movies));
            adapter.setItems(movies);
            adapter.notifyDataSetChanged();
        });
    }
}
