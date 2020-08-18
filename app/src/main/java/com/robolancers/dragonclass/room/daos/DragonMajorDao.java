package com.robolancers.dragonclass.room.daos;

import com.robolancers.dragonclass.room.entities.DragonMajor;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface DragonMajorDao {
    @Insert
    void insert(DragonMajor dragonMajor);

    @Query("DELETE FROM major_table")
    void deleteAll();

    @Query("SELECT * from major_table ORDER BY major_name ASC")
    LiveData<List<DragonMajor>> getAllMajors();
}
