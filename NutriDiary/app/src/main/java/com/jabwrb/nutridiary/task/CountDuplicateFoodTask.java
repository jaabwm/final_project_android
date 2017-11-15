package com.jabwrb.nutridiary.task;

import android.os.AsyncTask;

import com.jabwrb.nutridiary.database.Food;
import com.jabwrb.nutridiary.database.NutriDiaryDb;

public class CountDuplicateFoodTask extends AsyncTask<Food, Void, Integer> {

    private NutriDiaryDb db;
    private OnDuplicateFoodCountListener listener;
    private Food food;

    public interface OnDuplicateFoodCountListener {
        void onDuplicateFoodCounted(int count, Food food);
    }

    public CountDuplicateFoodTask(NutriDiaryDb db, OnDuplicateFoodCountListener listener) {
        this.db = db;
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(Food... foods) {
        this.food = foods[0];

        return db.foodDao().countDuplicateRows(food.getName(), food.getBrand());
    }

    @Override
    protected void onPostExecute(Integer integer) {
        listener.onDuplicateFoodCounted(integer, food);
    }
}
