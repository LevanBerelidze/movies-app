package com.levanb.movies_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.levanb.movies_app.R;
import com.levanb.movies_app.listener.MovieFavoriteStatusListener;
import com.levanb.movies_app.repository.datatype.Movie;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MovieListRecyclerAdapter extends RecyclerView.Adapter<MovieListRecyclerAdapter.MovieItemViewHolder> {
    private Context context;
    private MovieFavoriteStatusListener favoriteStatusListener;

    private List<Movie> movies;
    private Set<Movie> favorites;

    class MovieItemViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView poster;
        LikeButton favoriteButton;

        MovieItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_view_title);
            poster = itemView.findViewById(R.id.image_view_poster);
            favoriteButton = itemView.findViewById(R.id.button_favorite);
        }
    }

    public MovieListRecyclerAdapter(Context context, List<Movie> movies, Set<Movie> favorites) {
        this.movies = movies;
        this.favorites = favorites;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieItemViewHolder holder, int position) {
        Movie currentMovie = movies.get(position);
        holder.title.setText(currentMovie.getTitle());
        String posterUrl = currentMovie.getPosterUrl();
        if (posterUrl != null) {
            Glide.with(context).asBitmap().load(currentMovie.getPosterUrl()).into(holder.poster);
        }
        holder.favoriteButton.setLiked(favorites.contains(currentMovie));
        holder.favoriteButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Movie movie = movies.get(position);
                MovieListRecyclerAdapter.this
                        .favoriteStatusListener
                        .onMovieFavoriteStatusChanged(movie, true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Movie movie = movies.get(position);
                MovieListRecyclerAdapter.this
                        .favoriteStatusListener
                        .onMovieFavoriteStatusChanged(movie, false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setItems(List<Movie> movies) {
        this.movies = movies;
    }

    /**
     * @param favorites set of items that are marked as favorites
     * @return indices of modified items
     */
    public Iterable<Integer> setFavorites(Set<Movie> favorites) {
        // change field
        Set<Movie> currentFavorites = this.favorites;
        this.favorites = favorites;

        // find changed indices
        List<Integer> indicesOfModifiedItems = new ArrayList<>();

        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            if (currentFavorites.contains(movie) ^ favorites.contains(movie)) {
                indicesOfModifiedItems.add(i);
            }
        }

        return indicesOfModifiedItems;
    }

    public void setFavoriteStatusListener(MovieFavoriteStatusListener favoriteStatusListener) {
        this.favoriteStatusListener = favoriteStatusListener;
    }
}
