package com.example.lab1.adapters;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.lab1.R;
import com.example.lab1.model.MovieItem;

import java.util.ArrayList;
import java.util.List;

public class MoviesListAdapter extends ArrayAdapter<String> {
    private final String TAG = "Adapter";
    private final Fragment context;
    private final ArrayList<MovieItem> movies;

    public MoviesListAdapter(Fragment context, ArrayList<MovieItem> movies, List<String> textViewResourceId) {

        super(context.getContext(), R.layout.display_movie_item, textViewResourceId);

        this.context=context;
        this.movies=movies;
        Log.i(TAG, "Size: " + String.valueOf(textViewResourceId.size()));
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View rowView=inflater.inflate(
                R.layout.display_movie_item, null,true);

        ImageView image = (ImageView) rowView.findViewById(R.id.poster);
        ImageView deleteButton = (ImageView) rowView.findViewById(R.id.deleteButton);
        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        TextView yearText = (TextView) rowView.findViewById(R.id.year);
        TextView typeText = (TextView) rowView.findViewById(R.id.type);

        Glide.with(this.context).load(movies.get(position).getPoster()).into(image);

        titleText.setText(movies.get(position).getTitle());
        yearText.setText(movies.get(position).getYear());
        typeText.setText(movies.get(position).getType());
        Log.i(TAG, String.valueOf(position));

        Bundle bundle = new Bundle();
        bundle.putString("delete", movies.get(position).getTitle());

        deleteButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(position);
            }
        });

        return rowView;
    };

    private void deleteItem(int position) {
        movies.remove(position);
        notifyDataSetChanged();
    }

}


