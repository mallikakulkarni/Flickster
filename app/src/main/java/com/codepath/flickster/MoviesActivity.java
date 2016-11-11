package com.codepath.flickster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.flickster.adapter.MovieArrayAdapter;
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
    ListView lvItems;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        lvItems = (ListView) findViewById(R.id.lvMovies);
        movies = new ArrayList<>();
        movieArrayAdapter = new MovieArrayAdapter(this, movies);
        lvItems.setAdapter(movieArrayAdapter);

        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

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
        setupViewDetailsListener();
    }

    private void setupViewDetailsListener() {
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View item, int position, long id) {
                        viewDetails(movies.get(position));
                    }
                });
    }

    public void viewDetails(Movie movie) {
        Intent intent = new Intent(MoviesActivity.this, DetailsActivity.class);
        intent.putExtra("movie", movie);
    }

}
