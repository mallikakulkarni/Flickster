package com.codepath.flickster.activities;

import android.os.Bundle;
import android.util.Log;

import com.codepath.flickster.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mallikaa on 11/11/16.
 */

public class AutoPlayActivity extends YouTubeBaseActivity {
    private final String urlFirst = "https://api.themoviedb.org/3/movie/";
    private final String urlSecond = "/trailers?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private final String google_api_key = "AIzaSyBV-Q-hnfNKB3_eqDmJY_Xe6xwkaYlA4xg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_video);

        YouTubePlayerView youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.player);

        youTubePlayerView.initialize(google_api_key,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        final YouTubePlayer youTubePlayer, boolean b) {
                        int id = getIntent().getIntExtra("id", 0);
                        String url = urlFirst + id + urlSecond;
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url(url).build();
                        Call call = client.newCall(request);
                        final String ytSource = null;
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String newSrc = null;
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
                                        youTubePlayer.loadVideo(src);
                                    }
                                } catch (JSONException jsone) {
                                    Log.d("debug", "Cannot get JSON Reponse");
                                }
                            }
                        });
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }

}
