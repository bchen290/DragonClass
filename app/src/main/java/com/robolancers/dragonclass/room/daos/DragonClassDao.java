package com.robolancers.dragonclass.room.daos;

import com.robolancers.dragonclass.room.entities.DragonClass;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface DragonClassDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DragonClass... dragonClass);

    @Query("DELETE FROM class_table")
    void deleteAll();

    @Query("SELECT * from class_table ORDER BY course_id ASC")
    LiveData<List<DragonClass>> getAllClasses();

    @Query("SELECT * from class_table ORDER BY course_id ASC")
    List<DragonClass> getListOfAllClasses();

    @Query("SELECT * FROM class_table WHERE parent_major_name = :majorName ORDER BY course_id ASC")
    LiveData<List<DragonClass>> getClassesWithMajor(String majorName);
}
