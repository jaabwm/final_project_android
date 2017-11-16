package com.jabwrb.nutridiary.task;

import android.os.AsyncTask;

import com.jabwrb.nutridiary.database.Food;
import com.jabwrb.nutridiary.database.NutriDiaryDb;

public class CreateFoodTask extends AsyncTask<Food, Void, Long> {

    private NutriDiaryDb db;
    private OnFoodCreateListener listener;

    public interface OnFoodCreateListener {
        void onFoodCreated(Long id);
    }

    public CreateFoodTask(NutriDiaryDb db, OnFoodCreateListener listener) {
        this.db = db;
        this.listener = listener;
    }

    @Override
    protected Long doInBackground(Food... foods) {
        return db.foodDao().insert(foods[0]);
    }

    @Override
    protected void onPostExecute(Long id) {
        listener.onFoodCreated(id);
    }
}
