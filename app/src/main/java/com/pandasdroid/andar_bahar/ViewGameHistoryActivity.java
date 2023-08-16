package com.pandasdroid.andar_bahar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;
import com.pandasdroid.andar_bahar.databinding.ActivityViewGameHistoryBinding;
import com.pandasdroid.andar_bahar.room.MyApp;

import java.util.ArrayList;
import java.util.List;

public class ViewGameHistoryActivity extends AppCompatActivity {

    private ActivityViewGameHistoryBinding binding;
    private List<Long> timestamps = new ArrayList<Long>();
    GameInputHistoryAdapter gameInputHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewGameHistoryBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());


        timestamps = MyApp.database.gameInputHistoryDao().getTimestamps();
        gameInputHistoryAdapter = new GameInputHistoryAdapter(timestamps);
        binding.rvHistory.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.rvHistory.setAdapter(gameInputHistoryAdapter);


    }
}