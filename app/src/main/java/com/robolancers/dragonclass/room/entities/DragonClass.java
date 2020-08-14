package com.robolancers.dragonclass.room.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "class_table")
public class DragonClass implements Parcelable {
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

    protected DragonClass(Parcel in) {
        courseID = in.readString();
        courseName = in.readString();
        courseDescription = in.readString();
        coursePrerequisites = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(courseID);
        dest.writeString(courseName);
        dest.writeString(courseDescription);
        dest.writeString(coursePrerequisites);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DragonClass> CREATOR = new Creator<DragonClass>() {
        @Override
        public DragonClass createFromParcel(Parcel in) {
            return new DragonClass(in);
        }

        @Override
        public DragonClass[] newArray(int size) {
            return new DragonClass[size];
        }
    };
}
