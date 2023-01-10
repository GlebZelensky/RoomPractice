package com.example.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Game.class}, version = 1)
public abstract class GameDB extends RoomDatabase{

    public abstract GameDAO gameDAO();

    private static final String DB_NAME = "games.db";
    private static volatile GameDB INSTANCE = null;

    synchronized static GameDB get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = create(context, false);
        }
        return INSTANCE;
    }

    public static GameDB create(Context context, boolean memoryOnly) {
        RoomDatabase.Builder<GameDB> b;
        if (memoryOnly) {
            b = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                    GameDB.class);
        }
        else {
            b = Room.databaseBuilder(context.getApplicationContext(),
                    GameDB.class,
                    DB_NAME);
        }

        return b.build();
    }
}