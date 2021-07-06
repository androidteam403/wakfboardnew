package com.thresholdsoft.praanadhara.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.thresholdsoft.praanadhara.data.db.dao.SurveyDao;
import com.thresholdsoft.praanadhara.data.db.model.FarmerLands;
import com.thresholdsoft.praanadhara.data.db.model.SurveyEntity;
import com.thresholdsoft.praanadhara.data.db.model.SurveyStatusEntity;
import com.thresholdsoft.praanadhara.root.AppConstant;
import com.thresholdsoft.praanadhara.ui.propertycreation.model.PropertyData;
import com.thresholdsoft.praanadhara.ui.propertysurvey.model.PointDataTable;
import com.thresholdsoft.praanadhara.ui.propertysurvey.model.PolylineDataTable;


/**
 * Created on : March 30, 2020
 * Author     : JAGADEESH
 */
@Database(entities = {SurveyStatusEntity.class, FarmerLands.class, SurveyEntity.class, PropertyData.class, PointDataTable.class, PolylineDataTable.class}, version = 1, exportSchema = false)
@TypeConverters({PointDataTable.ImageUploadTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase mInstance;

    public synchronized static AppDatabase getDatabaseInstance(Context context) {
        if (mInstance == null) {
            mInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppConstant.DB_NAME)
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