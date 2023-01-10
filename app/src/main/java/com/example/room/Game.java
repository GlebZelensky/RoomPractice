package com.example.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "games")
public class Game {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int _id;

    @NonNull
    public String name;

    @NonNull
    public String vendor;

    @NonNull
    public float price;

    @Ignore
    public Game(@NonNull String name, @NonNull String vendor, float price) {
        this.name = name;
        this.vendor = vendor;
        this.price = price;
    }

    public Game(int _id, @NonNull String name, @NonNull String vendor, float price) {
        this._id = _id;
        this.name = name;
        this.vendor = vendor;
        this.price = price;
    }
}
