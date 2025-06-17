package com.example.petapp.utils;
import com.example.petapp.models.Pet;

public class GameManager {
    private Pet pet;

    public void setPet(Pet pet) { this.pet = pet; }
    public void feedPet() { if (pet != null) pet.feed(); }
    public void playWithPet() { if (pet != null) pet.play(); }
    public void letPetSleep() { if (pet != null) pet.sleep(); }
}