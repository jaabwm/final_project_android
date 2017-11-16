package com.jabwrb.nutridiary.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FoodDao {

    @Query("SELECT * FROM Food ORDER BY name")
    List<Food> getAll();

    @Query("SELECT COUNT(*) FROM Food WHERE name = :name AND brand = :brand")
    int countDuplicateRows(String name, String brand);

    @Query("SELECT id FROM Food WHERE name = :name AND brand = :brand")
    int getDuplicateFoodId(String name, String brand);

    @Insert
    long insert(Food food);
}
