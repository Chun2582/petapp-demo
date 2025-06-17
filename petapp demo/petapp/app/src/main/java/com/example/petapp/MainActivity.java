package com.example.petapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.PackageManager;

import com.example.petapp.handlers.PetUpdateHandler;
import com.example.petapp.models.Pet;
import com.example.petapp.utils.GameManager;
import com.example.petapp.utils.PreferencesHelper;
import com.example.petapp.utils.ReminderReceiver;
import com.example.petapp.utils.SoundManager;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private Pet myPet;
    private ImageView petImageView;
    private ProgressBar hungerBar, happinessBar, energyBar;
    private TextView statusText;
    private Button feedButton, playButton, sleepButton;

    // 排程相關
    private EditText scheduleMessageEditText;
    private TimePicker scheduleTimePicker;
    private Button setScheduleButton;
    private Button toggleScheduleButton;
    private LinearLayout scheduleSection;

    private PetUpdateHandler updateHandler;
    private SoundManager soundManager;
    private GameManager gameManager;
    private PreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        initializeManagers();
        loadPetData();
        updateUI();
        setupClickListeners();
        startUpdateLoop();

        statusText.setText("今天可能會下雨，記得帶把傘喔!!");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1001);
            }
        }
    }

    private void initializeViews() {
        petImageView = findViewById(R.id.pet_image);
        hungerBar = findViewById(R.id.hunger_progress);
        happinessBar = findViewById(R.id.happiness_progress);
        energyBar = findViewById(R.id.energy_progress);
        statusText = findViewById(R.id.status_text);
        feedButton = findViewById(R.id.feed_button);
        playButton = findViewById(R.id.play_button);
        sleepButton = findViewById(R.id.sleep_button);

        toggleScheduleButton = findViewById(R.id.toggle_schedule_button);
        scheduleSection = findViewById(R.id.schedule_section);
        scheduleMessageEditText = findViewById(R.id.schedule_message);
        scheduleTimePicker = findViewById(R.id.schedule_time_picker);
        setScheduleButton = findViewById(R.id.set_schedule_button);

        scheduleTimePicker.setIs24HourView(true);
    }

    private void initializeManagers() {
        preferencesHelper = new PreferencesHelper(this);
        soundManager = new SoundManager(this);
        gameManager = new GameManager();
    }

    private void loadPetData() {
        myPet = preferencesHelper.loadPetData();
        gameManager.setPet(myPet);
        updateHandler = new PetUpdateHandler(myPet, this);
    }

    private void setupClickListeners() {
        feedButton.setOnClickListener(v -> {
            updateHandler.feed();
            updateHandler.updateUI();
            soundManager.playFeedSound();
        });

        playButton.setOnClickListener(v -> {
            updateHandler.play();
            updateHandler.updateUI();
            soundManager.playPlaySound();
        });

        sleepButton.setOnClickListener(v -> {
            updateHandler.sleep();
            updateHandler.updateUI();
            soundManager.playSleepSound();
        });

        setScheduleButton.setOnClickListener(v -> {
            String message = scheduleMessageEditText.getText().toString();
            int hour = scheduleTimePicker.getHour();
            int minute = scheduleTimePicker.getMinute();
            if (!message.isEmpty()) {
                scheduleReminder(this, message, hour, minute);
                Toast.makeText(this, "提醒已設定完成！", Toast.LENGTH_SHORT).show();
            }
        });

        toggleScheduleButton.setOnClickListener(v -> {
            if (scheduleSection.getVisibility() == View.GONE) {
                scheduleSection.setVisibility(View.VISIBLE);
                toggleScheduleButton.setText("隱藏提醒設定");
            } else {
                scheduleSection.setVisibility(View.GONE);
                toggleScheduleButton.setText("顯示提醒設定");
            }
        });
    }

    private void startUpdateLoop() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateHandler.passTime();
                updateHandler.updateUI();
                handler.postDelayed(this, 10000);
            }
        }, 10000);
    }

    private void scheduleReminder(Context context, String message, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra("reminder_message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, (int) System.currentTimeMillis(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // 修正建議：API 31+ 要加判斷 canScheduleExactAlarms()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(context, "無法設定精確鬧鐘，請到設定開啟權限", Toast.LENGTH_LONG).show();
                return;
            }
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public void updateUI() {
        hungerBar.setProgress(myPet.getHunger());
        happinessBar.setProgress(myPet.getHappiness());
        energyBar.setProgress(myPet.getEnergy());

        if (myPet.getHunger() < 30) {
            statusText.setText("我好餓...🍴");
            petImageView.setImageResource(R.drawable.pet_hungry);
        } else if (myPet.getHappiness() < 30) {
            statusText.setText("好無聊喔...😐");
            petImageView.setImageResource(R.drawable.pet_sick);
        } else if (myPet.getEnergy() < 30) {
            statusText.setText("我好累...😴");
            petImageView.setImageResource(R.drawable.pet_sleepy);
        } else {
            statusText.setText("我很開心！😊");
            petImageView.setImageResource(R.drawable.pet_happy);
        }
    }
}
