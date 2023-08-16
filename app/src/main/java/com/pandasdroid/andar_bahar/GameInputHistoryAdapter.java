package com.pandasdroid.andar_bahar;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pandasdroid.andar_bahar.databinding.ItemGameHistoryBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class GameInputHistoryAdapter extends RecyclerView.Adapter<GameInputHistoryAdapter.ViewHolder> {
    private List<Long> data;

    public GameInputHistoryAdapter(List<Long> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGameHistoryBinding binding = ItemGameHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(String.valueOf(data.get(position)));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemGameHistoryBinding binding;

        ViewHolder(@NonNull ItemGameHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(String history) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy h:mm a", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(history));
            binding.tvGameID.setText(sdf.format(calendar.getTime()));
            // Bind other data fields to views using binding as needed
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ReviewGameHistoryActivity.class);
                    intent.putExtra("timestamp", history);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
