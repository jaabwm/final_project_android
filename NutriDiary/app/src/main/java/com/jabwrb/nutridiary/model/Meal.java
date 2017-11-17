package com.jabwrb.nutridiary.model;

import com.jabwrb.nutridiary.database.FoodEntryWithFood;

import java.util.ArrayList;
import java.util.List;

public class Meal {

    private List<FoodEntryWithFood> meal;

    public Meal() {
        this.meal = new ArrayList<>();
    }

    public List<FoodEntryWithFood> getMeal() {
        return meal;
    }

    public void setMeal(List<FoodEntryWithFood> meal) {
        this.meal = meal;
    }

    public void add(FoodEntryWithFood foodEntryWithFood) {
        meal.add(foodEntryWithFood);
    }

    public int size() {
        return meal.size();
    }

    public int getTotalCalories() {
        int sum = 0;
        for (FoodEntryWithFood f : meal) {
            sum += (int) (f.getFoodEntry().getAmount() * f.getFood().getCalories());
        }
        return sum;
    }

    public float getTotalFat() {
        float sum = 0;
        for (FoodEntryWithFood f : meal) {
            sum += f.getFoodEntry().getAmount() * f.getFood().getFat();
        }
        return sum;
    }

    public float getTotalCarbohydrates() {
        float sum = 0;
        for (FoodEntryWithFood f : meal) {
            sum += f.getFoodEntry().getAmount() * f.getFood().getCarbohydrates();
        }
        return sum;
    }

    public float getTotalProtein() {
        float sum = 0;
        for (FoodEntryWithFood f : meal) {
            sum += f.getFoodEntry().getAmount() * f.getFood().getProtein();
        }
        return sum;
    }
}
