package com.example.lab1.ui_fragments.movie_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.lab1.R;
import com.example.lab1.adapters.MoviesListAdapter;
import com.example.lab1.model.MovieItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MovieListFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movies_list, container, false);

        String fileName = "MoviesList.json";

        Gson gson = new Gson();
        Type listOfMoviesItemsType = new TypeToken<ArrayList<MovieItem>>() {}.getType();
        ArrayList<MovieItem> movies = new ArrayList<>();
        ArrayList<String> maintitle = new ArrayList<>();

        try {
            movies = gson.fromJson(ReadTextFile(fileName), listOfMoviesItemsType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (MovieItem movie: movies) {
            maintitle.add(movie.getTitle());
        }
        ListView list = root.findViewById(R.id.MoviesListView);
        MoviesListAdapter adapter = new MoviesListAdapter(this, movies, maintitle);
        list.setAdapter(adapter);

        return root;
    }

    public String ReadTextFile(String name) throws IOException {
        StringBuilder string = new StringBuilder();
        String line = "";
        InputStream is = getContext().getAssets().open(name);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        while (true) {
            try {
                if ((line = reader.readLine()) == null) break;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            string.append(line);
        }
        is.close();
        return string.toString();
    }
}