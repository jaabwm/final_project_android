package com.jabwrb.nutridiary.task;

import android.os.AsyncTask;

import com.jabwrb.nutridiary.database.Food;
import com.jabwrb.nutridiary.database.NutriDiaryDb;

public class CreateFoodTask extends AsyncTask<Food, Void, Void> {

    private NutriDiaryDb db;
    private OnFoodCreateListener listener;

    public interface OnFoodCreateListener {
        void onFoodCreated();
    }

    public CreateFoodTask(NutriDiaryDb db, OnFoodCreateListener listener) {
        this.db = db;
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(Food... foods) {
        db.foodDao().insert(foods[0]);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        listener.onFoodCreated();
    }
}
