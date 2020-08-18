package com.robolancers.dragonclass.room.viewmodels;

import android.app.Application;

import com.robolancers.dragonclass.room.entities.DragonMajor;
import com.robolancers.dragonclass.room.repositories.DragonMajorRepository;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class DragonMajorViewModel extends AndroidViewModel {
    private DragonMajorRepository dragonMajorRepository;

    private LiveData<List<DragonMajor>> allMajors;

    public DragonMajorViewModel(Application application) {
        super(application);
        dragonMajorRepository = new DragonMajorRepository(application);
        allMajors = dragonMajorRepository.getAllMajors();
    }

    public LiveData<List<DragonMajor>> getAllMajors() {
        return allMajors;
    }

    public void insert(DragonMajor dragonMajor) {
        dragonMajorRepository.insert(dragonMajor);
    }
}
