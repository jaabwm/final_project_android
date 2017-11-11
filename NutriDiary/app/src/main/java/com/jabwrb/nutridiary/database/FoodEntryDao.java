package com.jabwrb.nutridiary.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

@Dao
public interface FoodEntryDao {

    @Insert
    void insert(FoodEntry foodEntry);
}
