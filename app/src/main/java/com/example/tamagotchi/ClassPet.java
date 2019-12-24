package com.example.tamagotchi;

import android.net.Uri;

public class ClassPet {
    private String name;
    private int hunger;
    private int stamina;
    private int health;
    private int emotion;
    private int gold;
    private int growth;
    private Uri figure;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHunger() {
        return hunger;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getEmotion() {
        return emotion;
    }

    public void setEmotion(int emotion) {
        this.emotion = emotion;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getGrowth() {
        return growth;
    }

    public void setGrowth(int growth) {
        this.growth = growth;
    }

    public Uri getFigure() {
        return figure;
    }

    public void setFigure(Uri figure) {
        this.figure = figure;
    }

    public void EatFood(int price,int upHunger){
        /*gold = gold - price;*/
        hunger = hunger + upHunger;
    }
    public void EatMedicine(int price,int upHunger){
        /*gold = gold - price;*/
        health = health + upHunger;
    }

}
