package com.onegravity.contactpicker.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.onegravity.contactpicker.core.ContactImpl;
import com.onegravity.contactpicker.dao.ContactDao;

@Database(entities = {ContactImpl.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase instance;


    public abstract ContactDao contactDao();


    public static AppDataBase getAppDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDataBase.class,
                    "need-net-db")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
