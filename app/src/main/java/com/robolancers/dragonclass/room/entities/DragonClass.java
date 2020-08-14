package com.robolancers.dragonclass.room.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "class_table")
public class DragonClass {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "course_id")
    private String courseID;

    @NonNull
    @ColumnInfo(name = "course_name")
    private String courseName;

    @NonNull
    @ColumnInfo(name = "course_description")
    private String courseDescription;

    @NonNull
    @ColumnInfo(name = "course_prerequisites")
    private String coursePrerequisites;

    public DragonClass(@NonNull String courseID, @NonNull String courseName, @NonNull String courseDescription, @NonNull String coursePrerequisites) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.coursePrerequisites = coursePrerequisites;
    }

    @NonNull
    public String getCourseID() {
        return courseID;
    }

    @NonNull
    public String getCourseName() {
        return courseName;
    }

    @NonNull
    public String getCourseDescription() {
        return courseDescription;
    }

    @NonNull
    public String getCoursePrerequisites() {
        return coursePrerequisites;
    }
}
