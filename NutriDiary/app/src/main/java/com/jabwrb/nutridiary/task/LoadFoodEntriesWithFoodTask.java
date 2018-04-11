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

        return db.foodEntryDao().loadFoodEntriesWithFood(dayStart, dayEnd);
    }

    @Override
    protected void onPostExecute(List<FoodEntryWithFood> foodEntryWithFoodList) {
        listener.onFoodLoaded(foodEntryWithFoodList);
    }
}
