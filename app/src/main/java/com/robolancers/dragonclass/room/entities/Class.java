package com.robolancers.dragonclass.room.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "class_table")
public class Class {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "course_id")
    private String courseID;

    @NonNull
    @ColumnInfo(name = "course_description")
    private String courseDescription;

    @NonNull
    @ColumnInfo(name = "course_prerequisites")
    private String coursePrerequisites;

    public Class(@NonNull String courseID, @NonNull String courseDescription, @NonNull String coursePrerequisites) {
        this.courseID = courseID;
        this.courseDescription = courseDescription;
        this.coursePrerequisites = coursePrerequisites;
    }

    @NonNull
    public String getCourseID() {
        return courseID;
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
