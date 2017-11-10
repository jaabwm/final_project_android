package com.jabwrb.nutridiary;

public class Food{

    private int id;

    private String name;

    private int calories;

    private float fat;

    private float cholesterol;

    private float sodium;

    private float saturated_fat;

    private float carbohydrate;

    private float dietary_fiber;

    private float sugars;

    private float protein;

    // getters & setters
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

    public float getSaturated_fat() {
        return saturated_fat;
    }

    public void setSaturated_fat(float saturated_fat) {
        this.saturated_fat = saturated_fat;
    }

    public float getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(float carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public float getDietary_fiber() {
        return dietary_fiber;
    }

    public void setDietary_fiber(float dietary_fiber) {
        this.dietary_fiber = dietary_fiber;
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
