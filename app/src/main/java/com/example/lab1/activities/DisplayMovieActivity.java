package com.example.lab1.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lab1.R;
import com.example.lab1.model.MovieDescritionItem;
import com.example.lab1.model.MovieItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class DisplayMovieActivity extends Activity {
    private static final String MOVIE = "movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaymovie);
        Intent intent = getIntent();
        String movie = intent.getStringExtra(MOVIE);

        String fileName = movie + ".json";
        Gson gson = new Gson();

        MovieDescritionItem description = new MovieDescritionItem();
        Type MovieDescritionItemType = new TypeToken<MovieDescritionItem>() {}.getType();
        try {
            description = gson.fromJson(ReadTextFile(fileName), MovieDescritionItemType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView tv = (TextView) findViewById(R.id.desciption);
        tv.setText(description.toString());
        int drawableResourceId = 0;
        if (description.getPoster() != null){
            drawableResourceId = this.getResources().getIdentifier(
                    description.getPoster().toLowerCase().replace(".jpg", ""),
                "drawable", this.getPackageName());
            System.out.println(description.getPoster().toLowerCase());
        }
        ImageView poster = findViewById(R.id.poster_display);
        System.out.println("Image:" + drawableResourceId);
        poster.setImageResource(drawableResourceId);
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
}