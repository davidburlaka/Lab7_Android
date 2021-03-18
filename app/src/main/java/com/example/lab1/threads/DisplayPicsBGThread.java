package com.example.lab1.threads;

import android.util.Log;

import com.example.lab1.activities.DisplayMovieActivity;
import com.example.lab1.ui_fragments.pics.PicsFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DisplayPicsBGThread implements Runnable {

    private String search;

    public DisplayPicsBGThread() {}

    @Override
    public void run() {
        try {
            PicsFragment.getUrlResponse(getSearch());
        } catch (IOException ignored) {
        }
    }

    public String getSearch() {
        String URL_ENDPOINT_SERVER = "https://pixabay.com/api/?key=19193969-87191e5db266905fe8936d565&q=yellow+flowers&image_type=photo&per_page=27";
        try {
            URL url = new URL(URL_ENDPOINT_SERVER);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            InputStream responseStream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            //just in case there is more than one line
            while ((line = reader.readLine()) != null) {
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