package com.levanb.movies_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
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

        // register network callback
        registerNetworkCallback(new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                repository.onNetworkStateChanged(true);
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                repository.onNetworkStateChanged(false);
            }
        });

        // start fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.layout_container, new MoviePagerFragment())
                    .commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return true;
    }

    private void registerNetworkCallback(ConnectivityManager.NetworkCallback networkCallback) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build();

        if (connectivityManager != null) {
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
        }
    }
}
