package com.codepath.flickster.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
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

<<<<<<< HEAD
=======
import butterknife.BindView;
>>>>>>> 403e25e18ff4186bd00632f9fe092e97c453c3e2
import butterknife.ButterKnife;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movies);
        lvItems = binding.lvMovies;
        movies = new ArrayList<>();
        movieArrayAdapter = new MovieArrayAdapter(MoviesActivity.this, movies);
        lvItems.setAdapter(movieArrayAdapter);
        lvItems.setItemsCanFocus(true);

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
    }
}
