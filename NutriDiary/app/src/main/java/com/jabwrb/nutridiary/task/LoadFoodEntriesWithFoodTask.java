package com.jabwrb.nutridiary.task;

import android.os.AsyncTask;

import com.jabwrb.nutridiary.database.Food;
import com.jabwrb.nutridiary.database.FoodEntry;
import com.jabwrb.nutridiary.database.FoodEntryWithFood;
import com.jabwrb.nutridiary.database.NutriDiaryDb;

import java.util.Date;
import java.util.List;

public class LoadFoodEntriesWithFoodTask extends AsyncTask<Void, Void, List<FoodEntryWithFood>> {

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
    protected List<FoodEntryWithFood> doInBackground(Void... voids) {
        // Today at 00:00.00
        Date dayst = new Date();
        dayst.setHours(0);
        dayst.setMinutes(0);
        dayst.setSeconds(0);
        Date dayet = new Date();

        // Today at 23:59.59
        dayet.setHours(23);
        dayet.setMinutes(59);
        dayet.setSeconds(59);

        return db.foodEntryDao().loadFoodEntriesWithFood(dayst, dayet);
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
