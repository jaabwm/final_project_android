package com.jabwrb.nutridiary.task;

import android.os.AsyncTask;

import com.jabwrb.nutridiary.database.Food;
import com.jabwrb.nutridiary.database.NutriDiaryDb;

import java.util.List;

public class LoadMyFoodTask extends AsyncTask<Void, Void, List<Food>> {

    private NutriDiaryDb db;
    private OnFoodLoadListener listener;

    public interface OnFoodLoadListener {
        void onFoodLoaded(List<Food> foods);
    }

    public LoadMyFoodTask(NutriDiaryDb db, OnFoodLoadListener listener) {
        this.db = db;
        this.listener = listener;
    }

    @Override
    protected List<Food> doInBackground(Void... voids) {
        return db.foodDao().getAll();
    }

    @Override
    protected void onPostExecute(List<Food> foods) {
        listener.onFoodLoaded(foods);
    }
}
