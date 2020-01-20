package com.levanb.movies_app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.Explode;
import androidx.transition.Fade;

import com.levanb.movies_app.R;
import com.levanb.movies_app.adapter.MovieListRecyclerAdapter;
import com.levanb.movies_app.decoration.ShadowMarginItemDecoration;
import com.levanb.movies_app.listener.MovieFavoriteStatusListener;
import com.levanb.movies_app.repository.datatype.Movie;

import com.levanb.movies_app.viewmodel.MovieListViewModel;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class BaseMovieGridFragment extends Fragment
        implements MovieListRecyclerAdapter.MovieSelectionListener, MovieListRecyclerAdapter.ItemCountListener {
    MovieListRecyclerAdapter adapter;
    MovieListViewModel viewModel;
    private View emptyMoviesPlaceholder;

    private MovieFavoriteStatusListener listener;

    @Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_movie_grid, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View contentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(contentView, savedInstanceState);

        Context context = getContext();
        RecyclerView recyclerView = contentView.findViewById(R.id.recycler_view_movies);
        recyclerView.addItemDecoration(new ShadowMarginItemDecoration(16));
        emptyMoviesPlaceholder = contentView.findViewById(R.id.text_view_empty);
        emptyMoviesPlaceholder.setVisibility(View.VISIBLE);
        adapter = new MovieListRecyclerAdapter(context, new ArrayList<>(), new HashSet<>());

        // setup listeners
        adapter.setFavoriteStatusListener(listener);
        adapter.setCountListener(this);
        adapter.setMovieSelectionListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(adapter);
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

    @Override
    public void onMovieSelected(Movie movie, MovieListRecyclerAdapter.MovieItemViewHolder holder) {
        MovieDetailsFragment detailsFragment = MovieDetailsFragment.newInstance(movie);

        // setup shared transition
        detailsFragment.setSharedElementEnterTransition(new AutoTransition());
        detailsFragment.setEnterTransition(new Explode());
        setExitTransition(new Fade());
        detailsFragment.setSharedElementReturnTransition(new AutoTransition());

        FragmentActivity activity = getActivity();
        assert activity != null;

        String transitionName = "poster" + movie.getId();
        ViewCompat.setTransitionName(holder.getPoster(), transitionName);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(holder.getPoster(), holder.getPoster().getTransitionName())
                .replace(R.id.layout_container, detailsFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onItemCountChanged(int count) {
        if (count > 0) {
            emptyMoviesPlaceholder.setVisibility(View.GONE);
        } else {
            emptyMoviesPlaceholder.setVisibility(View.VISIBLE);
        }
    }
}
