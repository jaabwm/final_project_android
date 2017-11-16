package com.jabwrb.nutridiary.task;

import android.os.AsyncTask;

import com.jabwrb.nutridiary.database.Food;
import com.jabwrb.nutridiary.database.NutriDiaryDb;

public class GetDuplicateFoodIdTask extends AsyncTask<Food, Void, Integer> {

    private NutriDiaryDb db;
    private GetDuplicateFoodIdListener listener;
    private Food food;

    public interface GetDuplicateFoodIdListener {
        void OnDuplicateFoodIdGot(int id, Food food);
    }

    public GetDuplicateFoodIdTask(NutriDiaryDb db, GetDuplicateFoodIdListener listener) {
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
        listener.OnDuplicateFoodIdGot(integer, food);
    }
}
