package com.codepath.flickster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by mallikaa on 11/10/16.
 */

public class DetailsActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        String releaseDate = "Release Date: " + getIntent().getStringExtra("releaseDate");
        String title = getIntent().getStringExtra("title");
        int rating = getIntent().getIntExtra("rating", 0);
        String overview = getIntent().getStringExtra("overview");
        TextView tvTitle = (TextView) findViewById(R.id.tvDetailsTitle);
        tvTitle.setText(title);
        TextView tvOverview = (TextView) findViewById(R.id.tvDetailsOverview);
        tvOverview.setText(overview);
        RatingBar tvRating = (RatingBar) findViewById(R.id.tvDetailsRatingBar);
        tvRating.setNumStars(rating/2);
        TextView tvReleaseDate = (TextView) findViewById(R.id.tvDetailsReleaseDate);
        tvReleaseDate.setText(releaseDate);
    }

}
