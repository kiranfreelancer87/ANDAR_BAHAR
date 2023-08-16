package com.pandasdroid.andar_bahar.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {GameInputHistory.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GameInputHistoryDao gameInputHistoryDao();
}
