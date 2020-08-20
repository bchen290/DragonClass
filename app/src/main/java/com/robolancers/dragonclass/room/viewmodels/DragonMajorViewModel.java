package com.robolancers.dragonclass.room.viewmodels;

import android.app.Application;

import com.robolancers.dragonclass.room.entities.DragonMajor;
import com.robolancers.dragonclass.room.repositories.DragonMajorRepository;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class DragonMajorViewModel extends AndroidViewModel {
    private DragonMajorRepository dragonMajorRepository;

    private LiveData<List<DragonMajor>> allMajors;
    private LiveData<List<DragonMajor>> searchByLiveData;
    private MutableLiveData<DragonMajor> filterLiveData;

    public DragonMajorViewModel(Application application) {
        super(application);
        dragonMajorRepository = new DragonMajorRepository(application);
        allMajors = dragonMajorRepository.getAllMajors();

        filterLiveData = new MutableLiveData<>();
        searchByLiveData = Transformations.switchMap(filterLiveData, v -> dragonMajorRepository.searchByMajorName(v.getMajorName()));
    }

    public LiveData<List<DragonMajor>> getAllMajors() {
        return allMajors;
    }

    public void insert(DragonMajor dragonMajor) {
        dragonMajorRepository.insert(dragonMajor);
    }
}
