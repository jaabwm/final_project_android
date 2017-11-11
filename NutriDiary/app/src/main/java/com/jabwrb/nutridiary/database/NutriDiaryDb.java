package com.jabwrb.nutridiary.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Food.class, FoodEntry.class}, version = 1)
public abstract class NutriDiaryDb extends RoomDatabase {
    public abstract FoodDao foodDao();
    public abstract FoodEntryDao foodEntryDao();
}
