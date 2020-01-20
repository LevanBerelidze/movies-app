package com.levanb.movies_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.levanb.movies_app.R;
import com.levanb.movies_app.api.MovieService;
import com.levanb.movies_app.data.MovieDatabase;
import com.levanb.movies_app.fragment.MoviePagerFragment;
import com.levanb.movies_app.repository.MovieRepository;
import com.levanb.movies_app.viewmodel.MovieListViewModel;
import com.levanb.movies_app.viewmodel.MovieListViewModelFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieListActivity extends AppCompatActivity {
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

        // start fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.layout_container, new MoviePagerFragment())
                    .commit();
        }
    }
}
