package com.robolancers.dragonclass.room;

import android.content.Context;

import com.robolancers.dragonclass.room.daos.DragonClassDao;
import com.robolancers.dragonclass.room.daos.DragonMajorDao;
import com.robolancers.dragonclass.room.entities.DragonClass;
import com.robolancers.dragonclass.room.entities.DragonMajor;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {DragonClass.class, DragonMajor.class}, version = 1, exportSchema = false)
public abstract class DragonClassDatabase extends RoomDatabase {
    public abstract DragonClassDao dragonClassDao();
    public abstract DragonMajorDao dragonMajorDao();

    private static DragonClassDatabase instance;

    public static DragonClassDatabase getDatabase(final Context context) {
        if(instance == null) {
            synchronized (DragonClassDatabase.class) {
                // Check again after synchronization
                if(instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), DragonClassDatabase.class, "dragon_class_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return instance;
    }
}
