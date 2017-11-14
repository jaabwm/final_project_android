package com.jabwrb.nutridiary.task;

import android.os.AsyncTask;

import com.jabwrb.nutridiary.database.FoodEntry;
import com.jabwrb.nutridiary.database.NutriDiaryDb;

public class DeleteFoodEntryTask extends AsyncTask<FoodEntry, Void, Void> {

    private NutriDiaryDb db;
    private DeleteFoodEntryTask.OnFoodEntryDeleteListener listener;

    public interface OnFoodEntryDeleteListener {
        void onFoodEntryDeleted();
    }

    public DeleteFoodEntryTask(NutriDiaryDb db, OnFoodEntryDeleteListener listener) {
        this.db = db;
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(FoodEntry... foodEntries) {
        db.foodEntryDao().delete(foodEntries[0]);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        listener.onFoodEntryDeleted();
    }
}
