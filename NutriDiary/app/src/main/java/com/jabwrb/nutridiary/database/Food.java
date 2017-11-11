package com.jabwrb.nutridiary.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Food {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String brand;

    private int servingSizeUnit;

    private String servingSizeMeasurement;

    private int calories;

    private float fat;

    private float cholesterol;

    private float sodium;

    private float saturatedFat;

    private float carbohydrates;

    private float dietaryFiber;

    private float sugars;

    private float protein;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getServingSizeUnit() {
        return servingSizeUnit;
    }

    public void setServingSizeUnit(int servingSizeUnit) {
        this.servingSizeUnit = servingSizeUnit;
    }

    public String getServingSizeMeasurement() {
        return servingSizeMeasurement;
    }

    public void setServingSizeMeasurement(String servingSizeMeasurement) {
        this.servingSizeMeasurement = servingSizeMeasurement;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(float cholesterol) {
        this.cholesterol = cholesterol;
    }

    public float getSodium() {
        return sodium;
    }

    public void setSodium(float sodium) {
        this.sodium = sodium;
    }

    public float getSaturatedFat() {
        return saturatedFat;
    }

    public void setSaturatedFat(float saturatedFat) {
        this.saturatedFat = saturatedFat;
    }

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public float getDietaryFiber() {
        return dietaryFiber;
    }

    public void setDietaryFiber(float dietaryFiber) {
        this.dietaryFiber = dietaryFiber;
    }

    public float getSugars() {
        return sugars;
    }

    public void setSugars(float sugars) {
        this.sugars = sugars;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }
}
