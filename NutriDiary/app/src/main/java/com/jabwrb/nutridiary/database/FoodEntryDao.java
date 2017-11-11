package com.jabwrb.nutridiary.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;
import java.util.List;

@Dao
@TypeConverters({DateConverter.class})
public interface FoodEntryDao {

    @Query("SELECT * FROM FoodEntry WHERE date BETWEEN :dayst AND :dayet")
    List<FoodEntryWithFood> loadFoodEntriesWithFood(Date dayst, Date dayet);

    @Insert
    void insert(FoodEntry foodEntry);
}
