package com.example.petapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.petapp.models.Pet;

public class PreferencesHelper {
    private static final String PREFS_NAME = "pet_preferences";
    private SharedPreferences prefs;

    public PreferencesHelper(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void savePetData(Pet pet) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("pet_name", pet.getName());
        editor.putInt("hunger", pet.getHunger());
        editor.putInt("happiness", pet.getHappiness());
        editor.putInt("energy", pet.getEnergy());
        editor.putLong("last_update_time", pet.getLastUpdateTime());
        editor.apply();
    }

    public Pet loadPetData() {
        String name = prefs.getString("pet_name", "阿毛");
        int hunger = prefs.getInt("hunger", 100);
        int happiness = prefs.getInt("happiness", 100);
        int energy = prefs.getInt("energy", 100);
        long lastUpdateTime = prefs.getLong("last_update_time", System.currentTimeMillis());

        Pet pet = new Pet(name);
        pet.setHunger(hunger);
        pet.setHappiness(happiness);
        pet.setEnergy(energy);
        pet.setLastUpdateTime(lastUpdateTime);

        return pet;
    }
}
