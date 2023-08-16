package com.pandasdroid.andar_bahar.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "game_input_history")
public class GameInputHistory {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String time;
    public long timestamp;
    public int input;

    public GameInputHistory(String time, long timestamp, int input) {
        this.time = time;
        this.timestamp = timestamp;
        this.input = input;
    }

    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getInput() {
        return input;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setInput(int input) {
        this.input = input;
    }
}
