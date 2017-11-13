package com.jabwrb.nutridiary.task;

import android.os.AsyncTask;

import com.jabwrb.nutridiary.database.Food;
import com.jabwrb.nutridiary.database.FoodEntry;
import com.jabwrb.nutridiary.database.FoodEntryWithFood;
import com.jabwrb.nutridiary.database.NutriDiaryDb;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LoadFoodEntriesWithFoodTask extends AsyncTask<Date, Void, List<FoodEntryWithFood>> {

    private NutriDiaryDb db;
    private OnFoodLoadListener listener;

    public interface OnFoodLoadListener {
        void onFoodLoaded(List<FoodEntryWithFood> foodEntryWithFoodList);
    }

    public LoadFoodEntriesWithFoodTask(NutriDiaryDb db, OnFoodLoadListener listener) {
        this.db = db;
        this.listener = listener;
    }

    @Override
    protected List<FoodEntryWithFood> doInBackground(Date... dates) {
        // Date at 00:00.00
        Date dayStart = new Date(dates[0].getTime());
        dayStart.setHours(0);
        dayStart.setMinutes(0);
        dayStart.setSeconds(0);

        // Date at 23:59.59
        Date dayEnd = new Date(dates[0].getTime());
        dayEnd.setHours(23);
        dayEnd.setMinutes(59);
        dayEnd.setSeconds(59);

        System.out.println("Query between " + dayStart.toString() + " and " + dayEnd.toString());

        return db.foodEntryDao().loadFoodEntriesWithFood(dayStart, dayEnd);
    }

    @Override
    protected void onPostExecute(List<FoodEntryWithFood> foodEntryWithFoodList) {
        listener.onFoodLoaded(foodEntryWithFoodList);

        //print data loaded
        System.out.println("------------------------------");
        for(FoodEntryWithFood feWithF : foodEntryWithFoodList) {
            List<Food> fs = feWithF.getFoods();
            FoodEntry fe = feWithF.getFoodEntry();

            System.out.printf("Entry no.%d (foodId: %d, meal: %s, date: %s)\n",
                    fe.getId(), fe.getFoodId(), fe.getMeal(), fe.getDate().toString());

            for (Food f : fs) {
                System.out.printf("Food no.%d details (name: %s, brand: %s, cal: %d, fat: %f, carb: %f, protein: %f)\n\n",
                        f.getId(), f.getName(), f.getBrand(), f.getCalories(), f.getFat(), f.getCarbohydrates(), f.getProtein());
            }
        }
        System.out.println("------------------------------");
    }
}
