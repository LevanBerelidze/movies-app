package com.levanb.movies_app.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.levanb.movies_app.R;
import com.levanb.movies_app.adapter.MoviePagerAdapter;
import com.levanb.movies_app.api.MovieService;
import com.levanb.movies_app.data.MovieDatabase;
import com.levanb.movies_app.fragment.FavoriteMovieGridFragment;
import com.levanb.movies_app.fragment.BaseMovieGridFragment;
import com.levanb.movies_app.fragment.PopularMovieGridFragment;
import com.levanb.movies_app.fragment.TopRatedMovieGridFragment;
import com.levanb.movies_app.listener.MovieFavoriteStatusListener;
import com.levanb.movies_app.repository.MovieRepository;
import com.levanb.movies_app.repository.datatype.Movie;
import com.levanb.movies_app.viewmodel.MovieListViewModel;
import com.levanb.movies_app.viewmodel.MovieListViewModelFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieListActivity extends FragmentActivity
        implements MovieFavoriteStatusListener, MoviePagerAdapter.PageInfoProvider {
    public MovieListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        // manual DI
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MovieService service = retrofit.create(MovieService.class);
        MovieDatabase database = MovieDatabase.getInstance(this);
        MovieRepository repository = new MovieRepository(database, service);
        MovieListViewModelFactory factory = new MovieListViewModelFactory(repository);
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(MovieListViewModel.class);

        // initialize view pager (with tab layout)
        ViewPager pager = findViewById(R.id.view_pager_movies);
        TabLayout tabLayout = findViewById(R.id.tab_layout_movies);
        pager.setAdapter(new MoviePagerAdapter(
                this.getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                this
        ));
        pager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(pager);
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
