package com.example.lab1.threads;

import android.util.Log;

import com.example.lab1.activities.DisplayMovieActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DisplayMovieBGThread implements Runnable {

    private String search;

    public DisplayMovieBGThread(String search) { this.search = search; }

    @Override
    public void run() {
        try {
            DisplayMovieActivity.getUrlResponse(getSearch());
        } catch (IOException ignored) {
        }
    }

    public String getSearch() {
        String URL_ENDPOINT_SERVER = String.format("http://www.omdbapi.com/?apikey=7e9fe69e&i=%s", search);
        try {
            URL url = new URL(URL_ENDPOINT_SERVER);
            Log.i("NewsDataLoader", url.toString());
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            InputStream responseStream = connection.getInputStream();

            if (responseStream.available() < 0)
                Log.i("NewsDataLoader", "istream is available");
            else Log.i("NewsDataLoader", "istream is not available");
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            //just in case there is more than one line
            while ((line = reader.readLine()) != null) {
                Log.i("NewsDataLoader", "Reading line");
                stringBuilder.append(line);
            }
            connection.disconnect();
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}