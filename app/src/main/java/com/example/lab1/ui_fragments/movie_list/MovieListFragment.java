package com.example.lab1.ui_fragments.movie_list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import androidx.room.Room;

import com.example.lab1.R;
import com.example.lab1.activities.AddMovieActivity;
import com.example.lab1.activities.DisplayMovieActivity;
import com.example.lab1.adapters.MoviesListAdapter;
import com.example.lab1.dao.MoviesDao;
import com.example.lab1.database.MoviesDatabase;
import com.example.lab1.model.MovieEntity;
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
import java.util.Arrays;
import java.util.List;

import com.example.lab1.model.SearchItem;
import com.example.lab1.threads.MoviesBGThread;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

public class MovieListFragment extends Fragment {
    private String TAG = "MovieList";
    private static final String MOVIE = "movie";
    private static final String RESULT = "result";
    private static String moviesJSON = "";

    ArrayList<MovieItem> movies = new ArrayList<>();
    ArrayList<MovieEntity> moviesEntity = new ArrayList<>();
    SearchItem searchItem = new SearchItem();
    ArrayList<MovieItem> searchedList = new ArrayList<>();
    ListView list;
    MoviesListAdapter adapter;
    ArrayList<String> textViewResourceId = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movies_list, container, false);
        TextView nothingFound = root.findViewById(R.id.nothingFound);
        TextView noInternet = root.findViewById(R.id.noInternetText);
        nothingFound.setVisibility(View.INVISIBLE);
        noInternet.setVisibility(View.INVISIBLE);
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
                    noInternet.setVisibility(View.INVISIBLE);
                    try {
                        if (isNetworkAvailable()) {
                            fillListFromInternet(newText);
                        } else {
                            fillFromDatabase(newText, noInternet);
                        }
                    }
                    catch (InterruptedException e){
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

        addItemButton.setOnClickListener(v -> {
            try {
                openAddMovieActivity();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return root;
    }

    private void fillListFromInternet(String search) throws InterruptedException {
        Gson gson = new Gson();
        MoviesDatabase db = MoviesDatabase.getDbInstance(getContext());
        Type listOfMoviesItemsType = new TypeToken<SearchItem>() {}.getType();
        MoviesBGThread g = new MoviesBGThread(search);
        Thread t = new Thread(g, "Background Thread");
        t.start();//we start the thread
        t.join();
        System.out.println(moviesJSON);
        try {
            System.out.println("Query: " + db.moviesDao().findBySearch(search).toString());
        } catch (NullPointerException e){
            e.printStackTrace();
        }

        if (moviesJSON.contains("\"Response\":\"False\""))
            throw new InterruptedException();
        searchItem = gson.fromJson(moviesJSON, listOfMoviesItemsType);
        searchedList = searchItem.getMovies();
        updateResourseId(searchedList);

        //adding to db missing searches
        if (db.moviesDao() != null && db.moviesDao().findBySearch(search) == null){
            for (MovieItem movie : searchedList) {
                System.out.println(movie.toEntity(search));
                db.moviesDao().insertAll(movie.toEntity(search));
            }
        }

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

    public void fillFromDatabase(String search, TextView tv) throws InterruptedException{
        System.out.println("Connection is not available");
        MoviesDatabase db = MoviesDatabase.getDbInstance(getContext());
        if (db.moviesDao().findBySearch(search) != null) {
            moviesEntity = new ArrayList<MovieEntity>(db.moviesDao().getAllBySearch(search));
            searchedList.clear();
            for (MovieEntity entity : moviesEntity) {
                searchedList.add(entity.toItem());
                System.out.println("Converting to item: "+entity.toItem().toString());
            }
            System.out.println("Converted to item: "+searchedList);
            adapter = new MoviesListAdapter(this, searchedList, textViewResourceId);
            list.setAdapter(adapter);
            updateResourseId(searchedList);
            list.setOnItemClickListener((parent, view, position, id) -> {
                Object listItem = list.getItemAtPosition(position);
                openDisplayMovieActivity(id);
            });
        }
        else {
            tv.setVisibility(View.VISIBLE);
            throw new InterruptedException();
        }
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}