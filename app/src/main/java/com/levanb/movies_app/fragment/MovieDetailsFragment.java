package com.levanb.movies_app.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.levanb.movies_app.R;
import com.levanb.movies_app.repository.datatype.Movie;
import com.levanb.movies_app.viewmodel.MovieListViewModel;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

public class MovieDetailsFragment extends Fragment {
    private MovieListViewModel viewModel;
    private MutableLiveData<Boolean> isFavorite;

    public static MovieDetailsFragment newInstance(Movie movie) {
        Bundle args = new Bundle();
        args.putSerializable("movie", movie);

        MovieDetailsFragment fragment = new MovieDetailsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public MovieDetailsFragment() {
        isFavorite = new MutableLiveData<>();
        isFavorite.postValue(false);
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        postponeEnterTransition();
        return inflater.inflate(R.layout.fragment_movie_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        assert args != null;
        Movie movie = (Movie) args.getSerializable("movie");
        assert movie != null;

        // listen to favorite status
        viewModel.getFavoriteMovieById(movie.getId()).observe(this, m -> isFavorite.postValue(m != null));

        // initialize views
        TextView title = view.findViewById(R.id.text_view_original_title);
        TextView releaseDate = view.findViewById(R.id.text_view_release_date);
        RatingBar ratingBar = view.findViewById(R.id.rating);
        TextView ratingTextView = view.findViewById(R.id.text_view_rating);
        TextView overview = view.findViewById(R.id.text_view_overview);

        // set name for shared view
        String transitionName = "poster" + movie.getId();
        ViewCompat.setTransitionName(view, transitionName);

        // initialize toolbar
        final Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity != null;
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(movie.getTitle());

        // set poster image
        setBackdropImage(view, movie.getBackdropUrl());

        // set details to views
        title.setText(movie.getOriginalTitle());
        releaseDate.setText(formatReleaseDate(movie.getReleaseDate()));
        ratingBar.setRating((float) movie.getRating());
        ratingTextView.setText(String.format("%.1f", movie.getRating()));
        overview.setText(movie.getOverview());

        // manage favorite status
        assert isFavorite.getValue() != null; // "false" value is posted in constructor
        FloatingActionButton favoriteButton = view.findViewById(R.id.fab_favorite);
        isFavorite.observe(this, value -> {
            // TODO: consider removing hide() and show() calls
            // reason: possible bug in library
            favoriteButton.hide();
            int resId = value ? R.drawable.ic_heart_filled : R.drawable.ic_heart_empty;
            favoriteButton.setImageResource(resId);
            favoriteButton.show();
        });
        favoriteButton.setOnClickListener(v -> {
            viewModel.setFavorite(movie, !isFavorite.getValue());
        });
    }

    private void setBackdropImage(View contentView, String backdropUrl) {
        ImageView poster = contentView.findViewById(R.id.image_view_movie_poster);
        if (backdropUrl != null) {
            Glide.with(contentView.getContext())
                    .asBitmap()
                    .load(backdropUrl)
                    .apply(new RequestOptions().placeholder(R.drawable.backdrop_placeholder))
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            Activity parent = getActivity();
                            if (parent != null) {
                                parent.startPostponedEnterTransition();
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            Activity parent = getActivity();
                            if (parent != null) {
                                parent.startPostponedEnterTransition();
                            }
                            return false;
                        }
                    })
                    .into(poster);
        } else {
            poster.setImageResource(R.drawable.backdrop_placeholder);
        }
    }

    private static String formatReleaseDate(Date releaseDate) {
        Calendar calendar = Calendar.getInstance();
        DateFormatSymbols symbols = new DateFormatSymbols();
        calendar.setTime(releaseDate);
        String month =  symbols.getMonths()[calendar.get(Calendar.MONTH)];
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        return String.format("%s %d, %d", month, day, year);
    }
}
