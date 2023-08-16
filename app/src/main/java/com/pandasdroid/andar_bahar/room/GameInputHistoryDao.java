package com.pandasdroid.andar_bahar.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GameInputHistoryDao {
    @Insert
    void insertGameInputHistory(GameInputHistory gameInputHistory);

    @Query("SELECT * FROM game_input_history ORDER BY id DESC")
    List<GameInputHistory> getAllGameInputHistory();

    @Query("SELECT * FROM game_input_history where timestamp = :timestamp ORDER BY id DESC")
    List<GameInputHistory> getAllGameInputByTimestamp(Long timestamp);

    @Query("DELETE FROM game_input_history WHERE id = (SELECT MAX(id) FROM game_input_history)")
    void deleteLastGameInputHistory();

    @Query("SELECT distinct timestamp FROM game_input_history")
    List<Long> getTimestamps();
}
