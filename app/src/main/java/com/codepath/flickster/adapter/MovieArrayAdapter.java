package com.codepath.flickster.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.flickster.R;
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
    private static class ViewHolder {
        ImageView imageView;
        TextView title;
        TextView overview;
    }

    public MovieArrayAdapter(Context context, List<Movie>movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = getConvertView(inflater, movie, parent);
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

    private View getConvertView(LayoutInflater inflater, Movie movie, ViewGroup parent) {
        if (movie.getRating() < 5) {
            return inflater.inflate(R.layout.item_movie, parent, false);
        }
        return inflater.inflate(R.layout.item_backdrop, parent, false);
    }

    private ViewHolder getViewFields(Movie movie, LayoutInflater inflater, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iVMovieThumbnail);
        viewHolder.title = (TextView) convertView.findViewById(tvTitle);
        viewHolder.overview = (TextView) convertView.findViewById(tvOverview);
        return viewHolder;
    }

}
