package com.pandasdroid.andar_bahar.room;

import android.app.Application;
import androidx.room.Room;

public class MyApp extends Application {
    public static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "game_input_history_db")
                .allowMainThreadQueries() // Only for demonstration, don't use on the main thread in production
                .build();
    }
}
