package com.example.petapp.models;

public class Pet {
    private String name;
    private int hunger;
    private int happiness;
    private int energy;
    private long lastUpdateTime;

    public Pet(String name) {
        this.name = name;
        this.hunger = 100;
        this.happiness = 100;
        this.energy = 100;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public void feed() {
        hunger = Math.min(hunger + 20, 100);
    }

    public void play() {
        happiness = Math.min(happiness + 20, 100);
        energy = Math.max(energy - 10, 0);
    }

    public void sleep() {
        energy = Math.min(energy + 30, 100);
        hunger = Math.max(hunger - 10, 0);
    }

    public void passTime() {
        hunger = Math.max(hunger - 5, 0);
        happiness = Math.max(happiness - 5, 0);
        energy = Math.max(energy - 5, 0);
    }

    // Getter
    public String getName() {
        return name;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public int getHunger() {
        return hunger;
    }

    public int getHappiness() {
        return happiness;
    }

    public int getEnergy() {
        return energy;
    }

    // Setter
    public void setName(String name) {
        this.name = name;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
}
