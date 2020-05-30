package com.thresholdsoft.praanadhara.data.db;

import android.content.Context;

import com.thresholdsoft.praanadhara.data.db.dao.SurveyDao;
import com.thresholdsoft.praanadhara.data.db.model.Survey;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


/**
 * Created on : March 30, 2020
 * Author     : JAGADEESH
 */
@Database(entities = {Survey.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "praanadhaara-database";
    private static AppDatabase mInstance;

    public synchronized static AppDatabase getDatabaseInstance(Context context) {
        if (mInstance == null) {
            mInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return mInstance;
    }

    public static void destroyInstance() {
        mInstance = null;
    }

    public abstract SurveyDao userDao();

}