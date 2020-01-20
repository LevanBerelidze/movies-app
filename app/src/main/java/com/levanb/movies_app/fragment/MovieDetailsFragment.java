package com.levanb.movies_app.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.levanb.movies_app.R;
import com.levanb.movies_app.repository.datatype.Movie;
import com.levanb.movies_app.viewmodel.MovieListViewModel;

public class MovieDetailsFragment extends Fragment {
    private MovieListViewModel viewModel;

    public static MovieDetailsFragment newInstance(Movie movie) {
        Bundle args = new Bundle();
        args.putSerializable("movie", movie);

        MovieDetailsFragment fragment = new MovieDetailsFragment();
        fragment.setArguments(args);

        return fragment;
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

        ImageView poster = view.findViewById(R.id.image_view_movie_poster);
        String transitionName = "poster" + movie.getId();
        ViewCompat.setTransitionName(poster, transitionName);
        TextView title = view.findViewById(R.id.text_view_title);
        TextView overview = view.findViewById(R.id.text_view_overview);

        // set poster image
        String backdropUrl = movie.getBackdropUrl();
        System.out.println(backdropUrl);
        if (backdropUrl != null) {
            Glide.with(view.getContext())
                    .asBitmap()
                    .load(backdropUrl)
                    .apply(new RequestOptions().placeholder(R.drawable.image_placeholder))
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            Activity parent = getActivity();
                            assert parent != null;
                            parent.startPostponedEnterTransition();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            Activity parent = getActivity();
                            assert parent != null;
                            parent.startPostponedEnterTransition();
                            return false;
                        }
                    })
                    .into(poster);
        }

        // set title and overview
        title.setText(movie.getTitle());
        overview.setText(movie.getOverview());
    }
}
