package com.jabwrb.nutridiary.task;

import android.os.AsyncTask;

import com.jabwrb.nutridiary.database.Food;
import com.jabwrb.nutridiary.database.NutriDiaryDb;

public class UpdateFoodTask extends AsyncTask<Food, Void, Void> {

    private NutriDiaryDb db;
    private OnFoodUpdateListener listener;

    public interface OnFoodUpdateListener {
        void onFoodUpdated();
    }

    public UpdateFoodTask(NutriDiaryDb db, OnFoodUpdateListener listener) {
        this.db = db;
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(Food... foods) {
        db.foodDao().update(foods[0]);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        listener.onFoodUpdated();
    }
}
