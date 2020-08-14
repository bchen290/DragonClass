package com.robolancers.dragonclass.room.viewmodels;

import android.app.Application;

import com.robolancers.dragonclass.room.entities.DragonClass;
import com.robolancers.dragonclass.room.repositories.DragonClassRepository;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class DragonClassViewModel extends AndroidViewModel {
    private DragonClassRepository dragonClassRepository;

    private LiveData<List<DragonClass>> allClasses;

    public DragonClassViewModel(Application application) {
        super(application);
        dragonClassRepository = new DragonClassRepository(application);
        allClasses = dragonClassRepository.getAllClasses();
    }

    public LiveData<List<DragonClass>> getAllClasses() {
        return allClasses;
    }

    public void insert(DragonClass dragonClass) {
        dragonClassRepository.insert(dragonClass);
    }
}
