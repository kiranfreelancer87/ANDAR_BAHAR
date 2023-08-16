package com.pandasdroid.andar_bahar;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.pandasdroid.andar_bahar.databinding.ActivityMainBinding;
import com.pandasdroid.andar_bahar.databinding.ActivityReviewBinding;
import com.pandasdroid.andar_bahar.databinding.LayoutInputBinding;
import com.pandasdroid.andar_bahar.room.GameInputHistory;
import com.pandasdroid.andar_bahar.room.MyApp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReviewGameHistoryActivity extends AppCompatActivity {

    private ActivityReviewBinding binding;

    int count = 0;

    ArrayList<Integer> inputs = new ArrayList<>();

    private final Handler handler = new Handler();

    private boolean isBlinking = false;
    LinearLayout llInput;
    // Initialize minTime and maxTime with the first item's time
    String minTime = "";
    String maxTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityReviewBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());


        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String currentDateFormatted = sdf.format(new Date());

        binding.tvDate.setText(currentDateFormatted);

        if (getIntent().getStringExtra("timestamp") == null) {
            finish();
        }

        binding.btnAndar.setOnClickListener(v -> {
            if (llInput == null || count % 4 == 0) {
                initLinearLayout();
            }

            count++;
            LayoutInputBinding b = LayoutInputBinding.inflate(getLayoutInflater());
            b.tv.setText("A");
            b.tv.setTextColor(Color.parseColor("#E53935"));
            llInput.addView(b.getRoot());
            inputs.add(0);
            // Scroll to the end
            binding.horScroll.post(() -> {
                int scrollAmount = binding.horScroll.getChildAt(0).getMeasuredWidth() - binding.horScroll.getWidth();
                if (scrollAmount > 0) {
                    binding.horScroll.scrollTo(scrollAmount, 0);
                }
            });
            prediction();
        });

        binding.btnBahar.setOnClickListener(v -> {
            if (llInput == null || count % 4 == 0) {
                initLinearLayout();
            }

            count++;
            LayoutInputBinding b = LayoutInputBinding.inflate(getLayoutInflater());
            b.tv.setText("B");
            b.tv.setTextColor(Color.parseColor("#00897B"));
            llInput.addView(b.getRoot());
            inputs.add(1);
            // Scroll to the end
            binding.horScroll.post(() -> {
                int scrollAmount = binding.horScroll.getChildAt(0).getMeasuredWidth() - binding.horScroll.getWidth();
                if (scrollAmount > 0) {
                    binding.horScroll.scrollTo(scrollAmount, 0);
                }
            });
            prediction();
        });


        // Time
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        List<GameInputHistory> list = MyApp.database.gameInputHistoryDao().getAllGameInputByTimestamp(Long.parseLong(getIntent().getStringExtra("timestamp")));

        list.forEach((d) -> {
            if (d.getInput() == 0) {
                binding.btnAndar.performClick();
            } else if (d.getInput() == 1) {
                binding.btnBahar.performClick();
            }
        });

        String minTime = list.get(0).getTime(); // Set initial minTime to the first entry's time
        String maxTime = list.get(0).getTime(); // Set initial maxTime to the first entry's time

        for (GameInputHistory history : list) {
            try {
                Date currentDate = sdf2.parse(history.getTime());
                Date minDate = sdf2.parse(minTime);
                Date maxDate = sdf2.parse(maxTime);

                if (currentDate.before(minDate)) {
                    minTime = history.getTime();
                }

                if (currentDate.after(maxDate)) {
                    maxTime = history.getTime();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.wtf("MinMax...", minTime + "," + maxTime);

        binding.tvStartTime.setText(minTime);
        binding.tvEndTime.setText(maxTime);
    }

    private void initLinearLayout() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(15, 10, 15, 0);

        llInput = new LinearLayout(this);
        llInput.setOrientation(LinearLayout.HORIZONTAL);
        llInput.setGravity(Gravity.CENTER);
        llInput.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        llInput.setLayoutParams(layoutParams);
        binding.llMain.addView(llInput);
    }

    public static String reverseString(String str) {
        StringBuilder reversed = new StringBuilder(str);
        reversed.reverse();
        return reversed.toString();
    }

    private void prediction() {

        if (count < 4) {
            return;
        }

        int show = -1;

        int n1 = inputs.get(inputs.size() - 1);
        int n2 = inputs.get(inputs.size() - 2);
        int n3 = inputs.get(inputs.size() - 3);
        int n4 = inputs.get(inputs.size() - 4);

        String str_4 = "" + n1 + n2 + n3 + n4;
        Log.wtf("OUTPUT.", str_4);

        if (reverseString(str_4).equals("0011")) {
            show = 0; // A: AABB
            Log.wtf("OUTPUT...", "AABB");
        } else if (reverseString(str_4).equals("1100")) {
            show = 1; // B: BBAA
            Log.wtf("OUTPUT...", "BBAA");
        } else if (reverseString(str_4).equals("0101")) {
            show = 0; // A: ABAB
            Log.wtf("OUTPUT...", "ABAB");
        } else if (reverseString(str_4).equals("1010")) {
            show = 1; // B: BABA
            Log.wtf("OUTPUT...", "BABA");
        } else if (n1 == 1 && n2 == 1 && n3 == 1 && n4 == 1) {
            show = 1; // B: BBBB
            Log.wtf("OUTPUT...", "BBBB");
        } else if (n1 == 0 && n2 == 0 && n3 == 0 && n4 == 0) {
            show = 0; // A: AAAA
            Log.wtf("OUTPUT...", "AAAA");
        } else if (n1 == 0 && n2 == 1 && n3 == 1 && n4 == 1) {
            show = 0; // B: ABBB
            Log.wtf("OUTPUT...", "BAAA");
        } else if (n1 == 1 && n2 == 0 && n3 == 0 && n4 == 0) {
            show = 1; // A: BAAA
            Log.wtf("OUTPUT...", "ABBB");
        } else if (reverseString(str_4).equals("1110")) {
            show = 1;
        } else if (reverseString(str_4).equals("0001")) {
            show = 0;
        }

        if (show != -1) {
            View view = new View(this);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5));
            view.setBackgroundColor(Color.BLACK);
        }
    }
}