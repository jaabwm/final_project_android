package com.jabwrb.nutridiary;

import com.jabwrb.nutridiary.database.Food;
import com.jabwrb.nutridiary.database.FoodEntry;
import com.jabwrb.nutridiary.database.FoodEntryWithFood;
import com.jabwrb.nutridiary.model.Meal;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MealTest {

    private FoodEntryWithFood createFoodEntryWithFood(float amount, int calories, float fat,
                                                      float carbohydrates, float protein) {
        FoodEntry foodEntry = new FoodEntry();
        foodEntry.setAmount(amount);

        Food food = new Food();
        food.setCalories(calories);
        food.setFat(fat);
        food.setCarbohydrates(carbohydrates);
        food.setProtein(protein);

        List<Food> foods = new ArrayList<>();
        foods.add(food);

        FoodEntryWithFood foodEntryWithFood = new FoodEntryWithFood();
        foodEntryWithFood.setFoodEntry(foodEntry);
        foodEntryWithFood.setFoods(foods);

        return foodEntryWithFood;
    }

    @Test
    public void sumOf150and200CaloriesShouldEquals350() {
        FoodEntryWithFood f1 = createFoodEntryWithFood(1.5f, 100, 0, 0, 0);
        FoodEntryWithFood f2 = createFoodEntryWithFood(1f, 200, 0, 0, 0);

        Meal meal = new Meal();
        meal.add(f1);
        meal.add(f2);

        int result = meal.getTotalCalories();

        assertEquals(350, result);
    }

    @Test
    public void totalCaloriesOfEmptyMealShouldEquals0() {
        Meal meal = new Meal();

        int result = meal.getTotalCalories();

        assertEquals(0, result);
    }

    @Test
    public void sumOf16dot49985and20dot1234FatShouldEquals36dot62325() {
        FoodEntryWithFood f1 = createFoodEntryWithFood(1.5f, 0, 10.9999f, 0, 0);
        FoodEntryWithFood f2 = createFoodEntryWithFood(1f, 0, 20.1234f, 0, 0);

        Meal meal = new Meal();
        meal.add(f1);
        meal.add(f2);

        float result = meal.getTotalFat();

        assertEquals(36.62325f, result, 0.0f);
    }

    @Test
    public void totalFatOfEmptyMealShouldEquals0() {
        Meal meal = new Meal();

        float result = meal.getTotalFat();

        assertEquals(0, result, 0.0f);
    }

    @Test
    public void sumOf16dot49985and20dot1234CarbsShouldEquals36dot62325() {
        FoodEntryWithFood f1 = createFoodEntryWithFood(1.5f, 0, 0, 10.9999f, 0);
        FoodEntryWithFood f2 = createFoodEntryWithFood(1f, 0, 0, 20.1234f, 0);

        Meal meal = new Meal();
        meal.add(f1);
        meal.add(f2);

        float result = meal.getTotalCarbohydrates();

        assertEquals(36.62325f, result, 0.0f);
    }

    @Test
    public void totalCarbsOfEmptyMealShouldEquals0() {
        Meal meal = new Meal();

        float result = meal.getTotalCarbohydrates();

        assertEquals(0, result, 0.0f);
    }

    @Test
    public void sumOf16dot49985and20dot1234ProteinShouldEquals36dot62325() {
        FoodEntryWithFood f1 = createFoodEntryWithFood(1.5f, 0, 0, 0, 10.9999f);
        FoodEntryWithFood f2 = createFoodEntryWithFood(1f, 0, 0, 0, 20.1234f);

        Meal meal = new Meal();
        meal.add(f1);
        meal.add(f2);

        float result = meal.getTotalProtein();

        assertEquals(36.62325f, result, 0.0f);
    }

    @Test
    public void totalProteinOfEmptyMealShouldEquals0() {
        Meal meal = new Meal();

        float result = meal.getTotalProtein();

        assertEquals(0, result, 0.0f);
    }
}
