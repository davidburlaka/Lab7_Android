package com.example.lab1.adapters;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.lab1.R;
import com.example.lab1.model.MovieItem;

import java.util.ArrayList;

public class MoviesListAdapter extends ArrayAdapter<String> {

    private final Fragment context;
    private final ArrayList<MovieItem> movies;
    private final ArrayList<String> maintitle;

    public MoviesListAdapter(Fragment context, ArrayList<MovieItem> movies, ArrayList<String> maintitle) {

        super(context.getContext(), R.layout.display_movie_item, maintitle);

        this.context=context;
        this.movies=movies;
        this.maintitle = maintitle;
        System.out.println("Got here con");
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View rowView=inflater.inflate(R.layout.display_movie_item, null,true);

        ImageView image = (ImageView) rowView.findViewById(R.id.poster);
        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        TextView yearText = (TextView) rowView.findViewById(R.id.year);
        TextView typeText = (TextView) rowView.findViewById(R.id.type);

        System.out.println(position);
        if (position == 1)
            image.setImageResource(R.drawable.poster_01);
        else if (position == 2)
            image.setImageResource(R.drawable.poster_02);
        else if (position ==  3)
            image.setImageResource(R.drawable.poster_03);
        else if (position ==  4)
            image.setImageResource(R.drawable.poster_05);
        else if (position ==  6)
            image.setImageResource(R.drawable.poster_06);
        else if (position ==  7)
            image.setImageResource(R.drawable.poster_07);
        else if (position ==  8)
            image.setImageResource(R.drawable.poster_08);
        else if (position ==  10)
            image.setImageResource(R.drawable.poster_10);
        else image.setImageResource(R.drawable.no_poster);

        titleText.setText(maintitle.get(position));
        yearText.setText(movies.get(position).getYear());
        typeText.setText(movies.get(position).getType());

        return rowView;
    };
}