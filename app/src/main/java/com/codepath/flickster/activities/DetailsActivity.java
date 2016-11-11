package com.codepath.flickster.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.flickster.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mallikaa on 11/10/16.
 */

public class DetailsActivity extends AppCompatActivity {
    String urlFirst = "https://api.themoviedb.org/3/movie/";
    String urlSecond = "/trailers?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    String google_api_key = "AIzaSyBV-Q-hnfNKB3_eqDmJY_Xe6xwkaYlA4xg";
    @BindView(R.id.tvDetailsTitle) TextView tvTitle;
    @BindView(R.id.tvDetailsOverview) TextView tvOverview;
    @BindView(R.id.tvDetailsRatingBar) RatingBar tvRating;
    @BindView(R.id.tvDetailsReleaseDate) TextView tvReleaseDate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        ButterKnife.bind(this);
        String releaseDate = "Release Date: " + getIntent().getStringExtra("releaseDate");
        String title = getIntent().getStringExtra("title");
        int rating = getIntent().getIntExtra("rating", 0);
        String overview = getIntent().getStringExtra("overview");
        tvTitle.setText(title);
        tvOverview.setText(overview);
        tvRating.setNumStars(rating/2);
        tvReleaseDate.setText(releaseDate);
        YouTubePlayerFragment youTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.tvDetailsVideoView);
        youTubePlayerFragment.initialize(google_api_key, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                String url = urlFirst + getIntent().getIntExtra("id", 0) + urlSecond;
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
                            JSONArray responseArray = jsonResponse.getJSONArray("youtube");
                            JSONObject jsonObject = null;
                            for (int i = 0; i < responseArray.length(); i++) {
                                try {
                                    jsonObject = responseArray.getJSONObject(i);
                                    if (jsonObject.get("type").equals("Trailer")) {
                                        break;
                                    }
                                } catch (JSONException jsone) {
                                    jsonObject = null;
                                }
                            }
                            if (jsonObject != null) {
                                String src = jsonObject.getString("source");
                                youTubePlayer.cueVideo(src);

                            }
                        } catch (JSONException jsone) {
                            Log.d("debug", "Cannot get JSON Reponse");
                        }
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }

}
