package com.example.lab1.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.lab1.model.MovieEntity;
import com.example.lab1.model.MovieItem;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MoviesDao {
    @Query("SELECT * FROM movies")
    List<MovieEntity> getAll();

    @Query("SELECT * FROM movies WHERE search LIKE :s LIMIT 3")
    MovieEntity findBySearch(String s);

    @Query("SELECT * FROM movies WHERE search LIKE :s")
    List<MovieEntity> getAllBySearch(String s);

    @Insert
    void insertAll(MovieEntity... movies);

    @Delete
    void delete(MovieEntity movies);

}
