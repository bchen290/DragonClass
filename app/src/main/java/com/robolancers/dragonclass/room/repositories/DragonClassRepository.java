package com.robolancers.dragonclass.room.repositories;

import android.app.Application;

import com.robolancers.dragonclass.room.DragonClassDatabase;
import com.robolancers.dragonclass.room.daos.DragonClassDao;
import com.robolancers.dragonclass.room.entities.DragonClass;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;

public class DragonClassRepository {
    private DragonClassDao dragonClassDao;
    private LiveData<List<DragonClass>> allClasses;

    DragonClassRepository(Application application) {
        DragonClassDatabase db = DragonClassDatabase.getDatabase(application);
        dragonClassDao = db.dragonClassDao();
        allClasses = dragonClassDao.getAllClasses();
    }

    public LiveData<List<DragonClass>> getAllClasses() {
        return allClasses;
    }

    public void insert(DragonClass dragonClass) {
        Executors.newSingleThreadExecutor().execute(() -> dragonClassDao.insert(dragonClass));
    }
}
