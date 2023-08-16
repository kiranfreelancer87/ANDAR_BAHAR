package com.pandasdroid.andar_bahar;

import androidx.appcompat.app.AppCompatActivity;

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

import com.pandasdroid.andar_bahar.R;
import com.pandasdroid.andar_bahar.TimeUtils;
import com.pandasdroid.andar_bahar.ViewGameHistoryActivity;
import com.pandasdroid.andar_bahar.databinding.ActivityMainBinding;
import com.pandasdroid.andar_bahar.databinding.LayoutInputBinding;
import com.pandasdroid.andar_bahar.room.GameInputHistory;
import com.pandasdroid.andar_bahar.room.MyApp;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    int count = 0;

    ArrayList<Integer> inputs = new ArrayList<>();

    private final Handler handler = new Handler();

    private boolean isBlinking = false;
    LinearLayout llInput;
    long currentTimestamp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        currentTimestamp = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String currentDateFormatted = sdf.format(new Date());

        binding.tvDate.setText(currentDateFormatted);

        binding.btnAndar.setOnClickListener(v -> {
            if (llInput == null || count % 4 == 0) {
                initLinearLayout();
            }
            GameInputHistory gameInputHistory = new GameInputHistory(TimeUtils.getCurrentTime(), currentTimestamp, 0);
            MyApp.database.gameInputHistoryDao().insertGameInputHistory(gameInputHistory);
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
            GameInputHistory gameInputHistory = new GameInputHistory(TimeUtils.getCurrentTime(), currentTimestamp, 1);
            MyApp.database.gameInputHistoryDao().insertGameInputHistory(gameInputHistory);

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

        binding.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTimestamp = System.currentTimeMillis();
                recreate();
            }
        });

        binding.btnBack.setOnClickListener((v) -> {

            if (llInput != null && llInput.getChildCount() > 0) {
                try {
                    stopBlinking();
                    mediaPlayer.pause(); // Pause the buzzer sound after the specified duration
                } catch (Exception e) {
                    e.printStackTrace();
                }
                count--;
                inputs.remove(inputs.size() - 1);
                MyApp.database.gameInputHistoryDao().deleteLastGameInputHistory();
                Log.wtf("ListSize...", "" + MyApp.database.gameInputHistoryDao().getAllGameInputHistory().size());
                llInput.removeViewAt(llInput.getChildCount() - 1);
                if (llInput.getChildCount() == 0) {
                    if (binding.llMain.getChildCount() > 0) {
                        binding.llMain.removeViewAt(binding.llMain.getChildCount() - 1);
                        if (binding.llMain.getChildCount() > 0) {
                            llInput = (LinearLayout) binding.llMain.getChildAt(binding.llMain.getChildCount() - 1);
                        }
                    }
                }
            }
        });

        binding.btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ViewGameHistoryActivity.class));
            }
        });

        mediaPlayer = MediaPlayer.create(this, R.raw.buzzer); // Load the buzzer sound from raw resources
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release(); // Release resources after playback is complete
            }
        });
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

        int n1 = inputs.get(inputs.size() - 4);
        int n2 = inputs.get(inputs.size() - 3);
        int n3 = inputs.get(inputs.size() - 2);
        int n4 = inputs.get(inputs.size() - 1);

        String str_4 = "" + n1 + n2 + n3 + n4;

        //baaa

        if (str_4.equals("0011")) {
            show = 0;
        } else if (str_4.equals("1100")) {
            show = 1;
        } else if (str_4.equals("0101")) {
            show = 0;
        } else if (str_4.equals("1010")) {
            show = 1;
        } else if (str_4.equals("1111")) {
            show = 1;
        } else if (str_4.equals("0000")) {
            show = 0;
        } else if (reverseString(str_4).equals("1000")) {
            show = 0;
        } else if (reverseString(str_4).equals("0111")) {
            show = 1;
        } else if (reverseString(str_4).equals("1110")) {
            show = 0;
        } else if (reverseString(str_4).equals("0001")) {
            show = 1;
        }
        
        if (show == 1) {
            binding.tvPrediction.setVisibility(View.VISIBLE);
            binding.tvPrediction.setText("PLAY BAHAR");
            binding.tvPrediction.setTextColor(Color.parseColor("#00897B"));
        } else if (show == 0) {
            binding.tvPrediction.setVisibility(View.VISIBLE);
            binding.tvPrediction.setText("PLAY ANDAR");
            binding.tvPrediction.setTextColor(Color.parseColor("#E53935"));
        }

        if (show != -1) {
            View view = new View(this);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5));
            view.setBackgroundColor(Color.BLACK);
            stopBlinking();
            startBlinking();
            playBuzzerForDuration();
        }

    }

    private void startBlinking() {
        binding.tvPrediction.setVisibility(View.VISIBLE);
        isBlinking = true;
        handler.postDelayed(blinkRunnable, 500); // Blink every 1 second
    }

    private final Runnable blinkRunnable = new Runnable() {
        @Override
        public void run() {
            if (binding.tvPrediction.getVisibility() == View.VISIBLE) {
                binding.tvPrediction.setVisibility(View.INVISIBLE);
            } else {
                binding.tvPrediction.setVisibility(View.VISIBLE);
            }

            if (isBlinking) {
                handler.postDelayed(this, 1000); // Schedule the next blink
            }
        }
    };

    private MediaPlayer mediaPlayer;

    private void playBuzzerForDuration() {
        mediaPlayer = MediaPlayer.create(this, R.raw.buzzer);

        // Release resources after playback is complete
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);

        mediaPlayer.start(); // Start playing the buzzer sound

        handler.postDelayed(() -> {
            try {
                stopBlinking();
                mediaPlayer.pause(); // Pause the buzzer sound after the specified duration
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 5000);
    }

    private void stopBlinking() {
        isBlinking = false;
        handler.removeCallbacks(blinkRunnable);
        binding.tvPrediction.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopBlinking();
    }
}