package com.example.lab1.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.lab1.dao.MoviesDao;
import com.example.lab1.model.MovieEntity;
import com.example.lab1.model.MovieItem;

@Database(entities = {MovieEntity.class}, version = 1)
public abstract class MoviesDatabase extends RoomDatabase {
    public abstract MoviesDao moviesDao();

    private static MoviesDatabase INSTANCE;

    public static MoviesDatabase getDbInstance(Context context) {

        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MoviesDatabase.class, "movies")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}