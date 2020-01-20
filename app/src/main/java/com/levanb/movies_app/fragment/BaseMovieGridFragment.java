package com.levanb.movies_app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.levanb.movies_app.R;
import com.levanb.movies_app.adapter.MovieListRecyclerAdapter;
import com.levanb.movies_app.decoration.ShadowMarginItemDecoration;
import com.levanb.movies_app.listener.MovieFavoriteStatusListener;
import com.levanb.movies_app.viewmodel.MovieListViewModel;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class BaseMovieGridFragment extends Fragment {
    MovieListRecyclerAdapter adapter;
    MovieListViewModel viewModel;

    private MovieFavoriteStatusListener listener;

    @Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        ViewGroup contentView = (ViewGroup)
                inflater.inflate(R.layout.fragment_movie_grid, container, false);

        // initialize UI elements
        Context context = getContext();
        RecyclerView recyclerView = contentView.findViewById(R.id.recycler_view_movies);
        recyclerView.addItemDecoration(new ShadowMarginItemDecoration(16));
        adapter = new MovieListRecyclerAdapter(context, new ArrayList<>(), new HashSet<>());
        adapter.setFavoriteStatusListener(listener);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(adapter);

        return contentView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        FragmentActivity activity = this.getActivity();
        if (activity == null) {
            throw new IllegalStateException();
        }
        viewModel = ViewModelProviders.of(activity).get(MovieListViewModel.class);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void setFavoriteStatusListener(MovieFavoriteStatusListener favoriteStatusListener) {
        listener = favoriteStatusListener;
        if (adapter != null) {
            adapter.setFavoriteStatusListener(listener);
        }
    }
}
