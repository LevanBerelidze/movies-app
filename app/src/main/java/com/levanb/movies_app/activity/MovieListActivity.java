package com.levanb.movies_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.levanb.movies_app.R;
import com.levanb.movies_app.adapter.MovieListRecyclerAdapter;
import com.levanb.movies_app.api.MovieService;
import com.levanb.movies_app.data.MovieDatabase;
import com.levanb.movies_app.repository.MovieRepository;
import com.levanb.movies_app.viewmodel.MovieListViewModel;
import com.levanb.movies_app.viewmodel.MovieListViewModelFactory;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        // manual "DI"
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MovieService service = retrofit.create(MovieService.class);
        MovieDatabase database = MovieDatabase.getInstance(this);
        MovieRepository repository = new MovieRepository(database, service);
        MovieListViewModelFactory factory = new MovieListViewModelFactory(repository);
        MovieListViewModel viewModel = ViewModelProviders
                .of(this, factory)
                .get(MovieListViewModel.class);

        // initialize UI elements
        RecyclerView recyclerView = findViewById(R.id.recycler_view_movies);
        MovieListRecyclerAdapter adapter = new MovieListRecyclerAdapter(this, new ArrayList<>());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);


        viewModel.getPopularMovies().observe(this, movies -> {
            adapter.updateItems(movies);
            adapter.notifyDataSetChanged();
        });
    }
}
