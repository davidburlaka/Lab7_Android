package com.example.lab1.ui_fragments.movie_list;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.lab1.R;
import com.example.lab1.activities.AddMovieActivity;
import com.example.lab1.activities.DisplayMovieActivity;
import com.example.lab1.adapters.MoviesListAdapter;
import com.example.lab1.model.MovieItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.example.lab1.model.SearchItem;
import com.example.lab1.threads.MoviesBGThread;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MovieListFragment extends Fragment {
    private String TAG = "MovieList";
    private static final String MOVIE = "movie";
    private static final String RESULT = "result";
    private static String moviesJSON = "";
    ArrayList<MovieItem> movies = new ArrayList<>();
    SearchItem searchItem = new SearchItem();
    ArrayList<MovieItem> searchedList = new ArrayList<>();
    ListView list;
    MoviesListAdapter adapter;
    ArrayList<String> textViewResourceId = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movies_list, container, false);

        String fileName = "MoviesList.json";

        TextView nothingFound = root.findViewById(R.id.nothingFound);
        nothingFound.setVisibility(View.INVISIBLE);
        list = root.findViewById(R.id.MoviesListView);
        SearchView searchView = root.findViewById(R.id.searchView2);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null)
                    adapter.clear();
                Log.i(TAG, String.valueOf(searchedList.size()));
                if (newText.length() >= 3){
                    nothingFound.setVisibility(View.INVISIBLE);
                    try {
                        fillList(newText);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        nothingFound.setVisibility(View.VISIBLE);
                    }
                }
                else
                    nothingFound.setVisibility(View.VISIBLE);
                return false;
            }
        });

        Button addItemButton = (Button) root.findViewById(R.id.addItem);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    openAddMovieActivity();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return root;
    }

    private void fillList(String search) throws InterruptedException {
        Gson gson = new Gson();
        Type listOfMoviesItemsType = new TypeToken<SearchItem>() {}.getType();
        MoviesBGThread g = new MoviesBGThread(search);
        Thread t = new Thread(g, "Background Thread");
        t.start();//we start the thread
        t.join();
        System.out.println(moviesJSON);
        if (moviesJSON.contains("\"Response\":\"False\""))
            throw new InterruptedException();
        searchItem = gson.fromJson(moviesJSON, listOfMoviesItemsType);
        searchedList = searchItem.getMovies();
        updateResourseId(searchedList);

        adapter = new MoviesListAdapter(this, searchedList, textViewResourceId);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = list.getItemAtPosition(position);
                openDisplayMovieActivity(id);
            }
        });
    }

    public void refresh() {
        updateResourseId(searchedList);
        adapter = new MoviesListAdapter(this, searchedList, textViewResourceId);
        list.setAdapter(adapter);
    }

    public void openDisplayMovieActivity(long id) {
        Intent intent = new Intent(this.getActivity(), DisplayMovieActivity.class);
        intent.putExtra(MOVIE, searchItem.getMovies().get((int) (id)).getImdbID());
        startActivity(intent);
    }

    public String ReadTextFile(String name) throws IOException {
        StringBuilder string = new StringBuilder();
        String line = "";
        try {
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

        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
        return string.toString();
    }

    public ArrayList<MovieItem> filter(String searchText) {
        ArrayList<MovieItem> newList = new ArrayList<>();

        for (MovieItem movie : movies) {
            if (movie.getTitle().contains(searchText) ||
                movie.getYear().contains(searchText) ||
                movie.getType().contains(searchText))
                newList.add(movie);
        }

        return newList;
    }

    private void updateResourseId (ArrayList<MovieItem> list) {
        textViewResourceId.clear();
        for (MovieItem movie: list) {
            textViewResourceId.add(movie.getTitle());
        }
    }

    public void openAddMovieActivity() throws IOException {
        Intent intent = new Intent(this.getActivity(), AddMovieActivity.class);
        intent.putExtra(MOVIE, moviesToString());
        startActivityForResult(intent, 1);
    }

    private void updateJSON(String newData) {
        Gson gson = new Gson();
        Type listOfMoviesItemsType = new TypeToken<ArrayList<MovieItem>>() {}.getType();
        Log.i(TAG, moviesToString());
        movies = gson.fromJson(newData, listOfMoviesItemsType);
        searchedList = movies;
        refresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String returnValue = data.getStringExtra(RESULT);
                updateJSON(returnValue);
            }
        }
    }

    private String moviesToString() {
        StringBuilder result = new StringBuilder("[ ");
        for (int i = 0; i < searchItem.getMovies().size(); i++) {
            if (i < searchItem.getMovies().size() - 1) {
                result.append(searchItem.getMovies().get(i).toString());
                result.append(", ");
            }
            else result.append(searchItem.getMovies().get(i).toString());

        }
        return result.append(" ]").toString();
    }

    public static void getUrlResponse(String search) throws IOException {
        moviesJSON = search;
    }

}