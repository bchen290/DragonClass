package com.robolancers.dragonclass.room.daos;

import com.robolancers.dragonclass.room.entities.DragonClass;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface DragonClassDao {
    @Insert
    void insert(DragonClass dragonClass);

    @Query("DELETE FROM class_table")
    void deleteAll();

    @Query("SELECT * from class_table ORDER BY course_id ASC")
    LiveData<List<DragonClass>> getAllClasses();
}
