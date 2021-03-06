package com.codepath.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mallikaa on 11/7/16.
 */

public class Movie {
    private String posterPath;
    private String originalTitle;
    private String overview;
    private int rating;
    private String backgroundImagePath;
    private String releaseDate;
    private int id;

    public Movie(JSONObject jsonObject) throws JSONException{
        this.posterPath = String.format("https://image.tmdb.org/t/p/w342%s", jsonObject.getString("poster_path"));
        this.backgroundImagePath = String.format("https://image.tmdb.org/t/p/w342%s", jsonObject.getString("backdrop_path"));
        this.originalTitle = jsonObject.getString("title");
        this.overview = jsonObject.getString("overview");
        this.rating = (int) Math.round(jsonObject.getDouble("vote_average"));
        this.releaseDate = jsonObject.getString("release_date");
        this.id = jsonObject.getInt("id");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getBackgroundImagePath() {
        return backgroundImagePath;
    }

    public void setBackgroundImagePath(String backgroundImagePath) {
        this.backgroundImagePath = backgroundImagePath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public static List<Movie> getMovies(JSONArray jsonArray) {
        List<Movie> movieList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Movie movie = new Movie(jsonObject);
                movieList.add(movie);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return movieList;
    }
}
