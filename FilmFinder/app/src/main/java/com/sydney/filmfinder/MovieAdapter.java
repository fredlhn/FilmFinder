package com.sydney.filmfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MovieAdapter extends ArrayAdapter<Movie> {

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_list_item, parent, false);
        }

        TextView titleTextView = convertView.findViewById(R.id.movieTitleTextView);
        TextView ratingTextView = convertView.findViewById(R.id.movieRatingTextView);

        titleTextView.setText(movie.getTitle());
        ratingTextView.setText(String.valueOf(movie.getRating()));

        return convertView;
    }
}

