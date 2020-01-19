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
import com.levanb.movies_app.repository.datatype.Movie;

import java.util.List;

public class MovieListRecyclerAdapter extends RecyclerView.Adapter<MovieListRecyclerAdapter.MovieItemViewHolder> {
    private List<Movie> movies;
    private Context context;

    public class MovieItemViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView poster;

        public MovieItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_view_title);
            poster = itemView.findViewById(R.id.image_view_poster);
        }
    }

    public MovieListRecyclerAdapter(Context context, List<Movie> movies) {
        this.movies = movies;
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
        System.out.println("url: " + posterUrl);
        if (posterUrl != null) {
            Glide.with(context).asBitmap().load(currentMovie.getPosterUrl()).into(holder.poster);
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void updateItems(List<Movie> movies) {
        this.movies = movies;
    }
}
