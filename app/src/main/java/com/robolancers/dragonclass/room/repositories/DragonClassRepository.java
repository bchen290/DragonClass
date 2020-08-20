package com.robolancers.dragonclass.room.repositories;

import android.app.Application;

import com.robolancers.dragonclass.room.DragonClassDatabase;
import com.robolancers.dragonclass.room.daos.DragonClassDao;
import com.robolancers.dragonclass.room.entities.DragonClass;
import com.robolancers.dragonclass.room.entities.DragonMajor;
import com.robolancers.dragonclass.utilities.AppExecutors;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class DragonClassRepository {
    private DragonClassDao dragonClassDao;
    private LiveData<List<DragonClass>> allClasses;

    public DragonClassRepository(Application application) {
        DragonClassDatabase db = DragonClassDatabase.getDatabase(application);
        dragonClassDao = db.dragonClassDao();
        allClasses = dragonClassDao.getAllClasses();
    }

    public LiveData<List<DragonClass>> getAllClasses() {
        return allClasses;
    }

    public void insert(DragonClass dragonClass) {
        AppExecutors.getInstance().diskIO().execute(() -> dragonClassDao.insert(dragonClass));
    }
}
