package com.codepath.flickster.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.flickster.R;
import com.codepath.flickster.activities.AutoPlayActivity;
import com.codepath.flickster.activities.DetailsActivity;
import com.codepath.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.codepath.flickster.R.id.tvOverview;
import static com.codepath.flickster.R.id.tvTitle;

/**
 * Created by mallikaa on 11/7/16.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {
    private final int viewCount = 2;

    private static class ViewHolder {
        ImageView imageView;
        TextView title;
        TextView overview;
    }

    public MovieArrayAdapter(Context context, List<Movie>movies) {
        super(context, 0, movies);
    }

    @Override
    public int getViewTypeCount() {
        return viewCount;
    }

    // Get the type of View that will be created by getView(int, View, ViewGroup)
    // for the specified item.
    @Override
    public int getItemViewType(int position) {
        return getItem(position).getRating() < 5 ? 0 : 1;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final Movie movie = getItem(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            int type = getItemViewType(position);
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = getConvertView(type, inflater, movie, parent);
            viewHolder = getViewFields(movie, inflater, convertView, parent);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (movie.getRating() < 5) {
            viewHolder.title.setText(movie.getOriginalTitle());
            viewHolder.overview.setText(movie.getOverview());
        }
        fitImageToOrientation(movie, viewHolder);
        notifyDataSetChanged();
        convertView.setClickable(true);
        convertView.setFocusable(true);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Movie movie1 = movie;
                if (movie.getRating() < 5) {
                    viewDetails(movie1);
                } else {
                    loadYoutubeActivity(movie1);
                }
            }
        });
        return convertView;
    }

    private void fitImageToOrientation(Movie movie, ViewHolder viewHolder) {
        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT && movie.getRating() < 5) {
            Picasso.with(getContext()).load(movie.getPosterPath()).transform(new RoundedCornersTransformation(5, 5)).into(viewHolder.imageView);
        } else {
            Picasso.with(getContext()).load(movie.getBackgroundImagePath()).transform(new RoundedCornersTransformation(5, 5)).into(viewHolder.imageView);
        }
    }

    private View getConvertView(int type, LayoutInflater inflater, Movie movie, ViewGroup parent) {
        if (type == 0) {
            return inflater.inflate(R.layout.item_movie, parent, false);
        } else if (type == 1) {
            return inflater.inflate(R.layout.item_backdrop, parent, false);
        } else if (type == 2) {
            return inflater.inflate(R.layout.movie_video, parent, false);
        }
        return null;
    }

    private ViewHolder getViewFields(Movie movie, LayoutInflater inflater, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iVMovieThumbnail);
        viewHolder.title = (TextView) convertView.findViewById(tvTitle);
        viewHolder.overview = (TextView) convertView.findViewById(tvOverview);
        return viewHolder;
    }

    private void viewDetails(Movie movie) {
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("title", movie.getOriginalTitle());
        intent.putExtra("rating", movie.getRating());
        intent.putExtra("overview", movie.getOverview());
        intent.putExtra("releaseDate", movie.getReleaseDate());
        intent.putExtra("id", movie.getId());
        getContext().startActivity(intent);
    }

    private void loadYoutubeActivity(Movie movie) {
        Intent intent = new Intent(getContext(), AutoPlayActivity.class);
        intent.putExtra("id", movie.getId());
        getContext().startActivity(intent);
    }

}
