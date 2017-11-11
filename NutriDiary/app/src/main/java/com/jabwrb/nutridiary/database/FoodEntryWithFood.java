package com.jabwrb.nutridiary.database;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class FoodEntryWithFood {
    @Embedded
    private FoodEntry foodEntry;

    @Relation(parentColumn = "foodId", entityColumn = "id", entity = Food.class)
    private List<Food> foods;

    public FoodEntry getFoodEntry() {
        return foodEntry;
    }

    public void setFoodEntry(FoodEntry foodEntry) {
        this.foodEntry = foodEntry;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }
}
