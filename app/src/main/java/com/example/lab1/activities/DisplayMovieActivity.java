package com.example.lab1.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lab1.R;
import com.example.lab1.model.MovieDescritionItem;
import com.example.lab1.threads.DisplayMovieBGThread;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

public class DisplayMovieActivity extends Activity {
    private static final String MOVIE = "movie";
    private static String movieInfoJSON = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaymovie);
        Intent intent = getIntent();
        String movie = intent.getStringExtra(MOVIE);

        Gson gson = new Gson();
        DisplayMovieBGThread g = new DisplayMovieBGThread(movie);
        Thread t = new Thread(g, "Background Thread");
        t.start();//we start the thread
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MovieDescritionItem description = new MovieDescritionItem();
        Type MovieDescritionItemType = new TypeToken<MovieDescritionItem>() {}.getType();
        System.out.println(movieInfoJSON);
        description = gson.fromJson(movieInfoJSON, MovieDescritionItemType);

        TextView tv = (TextView) findViewById(R.id.desciption);
        tv.setText(description.toString());

        if (description.getPoster() != null){
            ImageView poster = findViewById(R.id.poster_display);
            Glide.with(this).load(description.getPoster()).into(poster);
        }

    }

    public String ReadTextFile(String name) throws IOException {
        StringBuilder string = new StringBuilder();
        String line = "";
        InputStream is = this.getAssets().open(name);
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

    public static void getUrlResponse(String search) throws IOException {
        movieInfoJSON = search;
    }
}