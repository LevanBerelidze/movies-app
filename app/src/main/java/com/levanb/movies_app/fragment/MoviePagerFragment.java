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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.levanb.movies_app.R;
import com.levanb.movies_app.adapter.MoviePagerAdapter;
import com.levanb.movies_app.listener.MovieFavoriteStatusListener;
import com.levanb.movies_app.repository.datatype.Movie;
import com.levanb.movies_app.viewmodel.MovieListViewModel;

public class MoviePagerFragment extends Fragment
        implements MovieFavoriteStatusListener, MoviePagerAdapter.PageInfoProvider {
    private MovieListViewModel viewModel;
    private Snackbar snackbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View contentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(contentView, savedInstanceState);
        // get fragment manager (child manager is required because of nested fragments in pager)
        FragmentManager manager = getChildFragmentManager();

        // initialize view pager (with tab layout)
        ViewPager pager = contentView.findViewById(R.id.view_pager_movies);
        TabLayout tabLayout = contentView.findViewById(R.id.tab_layout_movies);
        pager.setAdapter(new MoviePagerAdapter(
                manager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                this
        ));
        tabLayout.setupWithViewPager(pager);

        // handle connection loss
        viewModel.isConnectedToNetwork().observe(this, isConnected -> {
            if (isConnected != null && !isConnected) {
                displayNetworkErrorSnackbar(contentView);
            } else {
                hideNetworkErrorSnackbar();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
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
    public void onPause() {
        super.onPause();
        hideNetworkErrorSnackbar();
    }

    @Override
    public void onMovieFavoriteStatusChanged(Movie movie, boolean isFavorite) {
        viewModel.setFavorite(movie, isFavorite);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getFragmentAt(int index) {
        switch (index) {
            case 0:
                return getFragmentOfPopularMovies();
            case 1:
                return getFragmentOfTopRatedMovies();
            case 2:
                return getFragmentOfFavoriteMovies();
        }
        return null;
    }

    private BaseMovieGridFragment getFragmentOfPopularMovies() {
        BaseMovieGridFragment fragment = new PopularMovieGridFragment();
        fragment.setFavoriteStatusListener(this);
        return fragment;
    }

    private BaseMovieGridFragment getFragmentOfTopRatedMovies() {
        BaseMovieGridFragment fragment = new TopRatedMovieGridFragment();
        fragment.setFavoriteStatusListener(this);
        return fragment;
    }

    private BaseMovieGridFragment getFragmentOfFavoriteMovies() {
        BaseMovieGridFragment fragment = new FavoriteMovieGridFragment();
        fragment.setFavoriteStatusListener(this);
        return fragment;
    }

    private void displayNetworkErrorSnackbar(View parent) {
        snackbar = Snackbar.make(parent, "Network is not available!", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Dismiss", v -> {
           hideNetworkErrorSnackbar();
        });
        snackbar.show();
    }

    private void hideNetworkErrorSnackbar() {
        if (snackbar != null) {
            snackbar.dismiss();
            snackbar = null;
        }
    }

    @Override
    public String getTitleAt(int index) {
        switch (index) {
            case 0:
                return "Popular";
            case 1:
                return "Top Rated";
            case 2:
                return "Favorite";
        }
        return "";
    }
}
