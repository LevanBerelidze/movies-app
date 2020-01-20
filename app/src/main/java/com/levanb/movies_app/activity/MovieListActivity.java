package com.levanb.movies_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;

import com.levanb.movies_app.R;
import com.levanb.movies_app.api.MovieService;
import com.levanb.movies_app.data.MovieDatabase;
import com.levanb.movies_app.fragment.MoviePagerFragment;
import com.levanb.movies_app.listener.NetworkStateListener;
import com.levanb.movies_app.repository.MovieRepository;
import com.levanb.movies_app.viewmodel.MovieListViewModel;
import com.levanb.movies_app.viewmodel.MovieListViewModelFactory;

import java.util.ArrayList;
import java.util.List;

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
        List<NetworkStateListener> networkStateListeners = new ArrayList<>();
        networkStateListeners.add(repository);
        networkStateListeners.add(viewModel);
        notifyInitialNetWorkState(networkStateListeners);
        registerNetworkCallback(networkStateListeners);

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

    private void registerNetworkCallback(Iterable<NetworkStateListener> listeners) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                for (NetworkStateListener listener : listeners) {
                    listener.onNetworkStateChanged(true);
                }
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                for (NetworkStateListener listener : listeners) {
                    listener.onNetworkStateChanged(false);
                }
            }
        };
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build();

        if (connectivityManager != null) {
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
        }
    }

    private void notifyInitialNetWorkState(Iterable<NetworkStateListener> listeners) {
        boolean isConnected = isNetworkAvailable();
        for (NetworkStateListener listener : listeners) {
            listener.onNetworkStateChanged(isConnected);
        }
    }

    private boolean isNetworkAvailable() {
        // TODO find another way to check connectivity
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
