package com.example.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GameDAO {

    @Query("SELECT * FROM games")
    List<Game> selectAll();

    @Query("SELECT * FROM games WHERE vendor = :vendor")
    List<Game> findByVendor(String vendor);

    @Query("DELETE FROM games")
    void deleteAll();

    @Insert
    void insert(Game... games);

    @Delete
    void delete(Game... games);

    @Update
    void update(Game... games);
}
