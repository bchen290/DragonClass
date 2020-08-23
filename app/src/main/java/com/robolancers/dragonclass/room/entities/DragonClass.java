package com.robolancers.dragonclass.room.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

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

    @NonNull
    @ColumnInfo(name = "parent_major_name")
    private String parentMajorName;

    @NonNull
    @ColumnInfo(name = "dependencies")
    private String dependencies;

    public DragonClass(@NonNull String courseID, @NonNull String courseName, @NonNull String courseDescription, @NonNull String coursePrerequisites, @NonNull String parentMajorName, @NonNull String dependencies) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.coursePrerequisites = coursePrerequisites;
        this.parentMajorName = parentMajorName;
        this.dependencies = dependencies;
    }

    protected DragonClass(Parcel in) {
        courseID = in.readString();
        courseName = in.readString();
        courseDescription = in.readString();
        coursePrerequisites = in.readString();
        parentMajorName = in.readString();
        dependencies = in.readString();
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

    @NonNull
    public String getParentMajorName() {
        return parentMajorName;
    }

    @NonNull
    public String getDependencies() {
        return dependencies;
    }

    @Override
    public String toString() {
        return courseID;
    }

    public void setDependencies(@NonNull String dependencies) {
        this.dependencies = dependencies;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(courseID);
        parcel.writeString(courseName);
        parcel.writeString(courseDescription);
        parcel.writeString(coursePrerequisites);
        parcel.writeString(parentMajorName);
        parcel.writeString(dependencies);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DragonClass that = (DragonClass) o;
        return courseID.equals(that.courseID) &&
                courseName.equals(that.courseName) &&
                courseDescription.equals(that.courseDescription) &&
                coursePrerequisites.equals(that.coursePrerequisites) &&
                parentMajorName.equals(that.parentMajorName) &&
                dependencies.equals(that.dependencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseID, courseName, courseDescription, coursePrerequisites, parentMajorName, dependencies);
    }
}
