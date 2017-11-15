package com.jabwrb.nutridiary.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FoodDao {

    @Query("SELECT * FROM Food")
    List<Food> getAll();

    @Query("SELECT COUNT(*) FROM Food WHERE name = :name AND brand = :brand")
    int countDuplicateRows(String name, String brand);

    @Insert
    void insert(Food food);
}
