package com.jabwrb.nutridiary.task;

import android.os.AsyncTask;

import com.jabwrb.nutridiary.database.FoodEntry;
import com.jabwrb.nutridiary.database.NutriDiaryDb;

public class AddToDiaryTask extends AsyncTask<FoodEntry, Void, Void> {

    private NutriDiaryDb db;
    private OnFoodAddListener listener;

    public interface OnFoodAddListener {
        void onFoodAdded();
    }

    public AddToDiaryTask(NutriDiaryDb db, OnFoodAddListener listener) {
        this.db = db;
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(FoodEntry... foodEntries) {
        db.foodEntryDao().insert(foodEntries[0]);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        listener.onFoodAdded();
    }
}
