package com.jabwrb.nutridiary.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;
import java.util.List;

@Dao
@TypeConverters({DateConverter.class})
public interface FoodEntryDao {

    @Query("SELECT * FROM FoodEntry WHERE date BETWEEN :dayStart AND :dayEnd")
    List<FoodEntryWithFood> loadFoodEntriesWithFood(Date dayStart, Date dayEnd);

    @Insert
    void insert(FoodEntry foodEntry);

    @Delete
    void delete(FoodEntry foodEntry);
}
