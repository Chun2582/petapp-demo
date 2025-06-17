package com.example.petapp.handlers;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.example.petapp.MainActivity;
import com.example.petapp.models.Pet;

public class PetUpdateHandler extends Handler {
    private Pet pet;
    private MainActivity activity;
    private static final int UPDATE_INTERVAL = 30000;

    public PetUpdateHandler(Pet pet, MainActivity activity) {
        super(Looper.getMainLooper());
        this.pet = pet;
        this.activity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if (pet != null && activity != null) {
            pet.passTime();
            activity.updateUI();
            sendEmptyMessageDelayed(0, UPDATE_INTERVAL);
        }
    }

    public void feed() {
        pet.feed();
    }

    public void play() {
        pet.play();
    }

    public void sleep() {
        pet.sleep();
    }

    public void passTime() {
        pet.passTime();
    }

    public void updateUI() {
        if (activity != null) {
            activity.updateUI();
        }
    }

    public void startUpdating() {
        sendEmptyMessageDelayed(0, UPDATE_INTERVAL);
    }

    public void stopUpdating() {
        removeCallbacksAndMessages(null);
    }
}
