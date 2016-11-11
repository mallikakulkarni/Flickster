package com.codepath.flickster.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.codepath.flickster.R;
import com.codepath.flickster.adapter.MovieArrayAdapter;
import com.codepath.flickster.databinding.ActivityMoviesBinding;
import com.codepath.flickster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MoviesActivity extends AppCompatActivity {
    List<Movie> movies;
    MovieArrayAdapter movieArrayAdapter;
    private ActivityMoviesBinding binding;
    ListView lvItems;
    private SwipeRefreshLayout swipeContainer;
    private final String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movies);
        lvItems = binding.lvMovies;
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.activity_main);
        movies = new ArrayList<>();
        movieArrayAdapter = new MovieArrayAdapter(MoviesActivity.this, movies);
        lvItems.setAdapter(movieArrayAdapter);
        lvItems.setItemsCanFocus(true);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Response invalid");
                }
                try {
                    JSONObject jsonResponse = new JSONObject(response.body().string());
                    JSONArray responseArray = jsonResponse.getJSONArray("results");
                    movies.addAll(Movie.getMovies(responseArray));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            movieArrayAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException jsone) {
                    Log.d("debug", "Cannot get JSON Reponse");
                }
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync(0);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void fetchTimelineAsync(int page) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Response invalid");
                }
                try {
                    JSONObject jsonResponse = new JSONObject(response.body().string());
                    JSONArray responseArray = jsonResponse.getJSONArray("results");
                    movies = new ArrayList<Movie>();
                    movies.addAll(Movie.getMovies(responseArray));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            movieArrayAdapter.notifyDataSetChanged();
                            swipeContainer.setRefreshing(false);
                        }
                    });
                } catch (JSONException jsone) {
                    Log.d("debug", "Cannot get JSON Reponse");
                }
            }
        });

    }
}
