package com.robolancers.dragonclass.room.repositories;

import android.app.Application;

import com.robolancers.dragonclass.room.DragonClassDatabase;
import com.robolancers.dragonclass.room.daos.DragonMajorDao;
import com.robolancers.dragonclass.room.entities.DragonMajor;
import com.robolancers.dragonclass.utilities.AppExecutors;

import java.util.List;
import java.util.stream.Collectors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class DragonMajorRepository {
    private DragonMajorDao dragonMajorDao;
    private LiveData<List<DragonMajor>> allMajors;

    public DragonMajorRepository(Application application) {
        DragonClassDatabase db = DragonClassDatabase.getDatabase(application);
        dragonMajorDao = db.dragonMajorDao();
        allMajors = dragonMajorDao.getAllMajors();
    }

    public LiveData<List<DragonMajor>> getAllMajors() {
        return allMajors;
    }

    public void insert(DragonMajor dragonMajor) {
        AppExecutors.getInstance().diskIO().execute(() -> dragonMajorDao.insert(dragonMajor));
    }

    public LiveData<List<DragonMajor>> searchByMajorName(String majorName) {
        MutableLiveData<List<DragonMajor>> temp = new MutableLiveData<>();
        temp.setValue(allMajors.getValue().stream().filter(dragonMajor -> dragonMajor.getMajorName().equals(majorName)).collect(Collectors.toList()));
        return temp;
    }
}