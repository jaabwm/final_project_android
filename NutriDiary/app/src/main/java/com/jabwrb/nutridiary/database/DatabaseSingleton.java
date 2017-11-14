package com.jabwrb.nutridiary.database;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DatabaseSingleton {

    private static DatabaseSingleton databaseInstance;
    private NutriDiaryDb db;

    private DatabaseSingleton() {
    }

    public static DatabaseSingleton getDatabaseInstance() {
        if (databaseInstance == null) {
            databaseInstance = new DatabaseSingleton();
        }
        return databaseInstance;
    }

    public void initDb(Context context) {
        db = Room.databaseBuilder(context,
                NutriDiaryDb.class, "NutriDiary.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    public NutriDiaryDb getDb() {
        return db;
    }
}
