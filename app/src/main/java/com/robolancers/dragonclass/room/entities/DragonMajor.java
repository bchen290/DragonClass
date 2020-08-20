package com.robolancers.dragonclass.room.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "major_table")
public class DragonMajor implements Parcelable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "major_name")
    private String majorName;

    public DragonMajor(@NonNull String majorName) {
        this.majorName = majorName;
    }

    @NonNull
    public String getMajorName() {
        return majorName;
    }

    protected DragonMajor(Parcel in) {
        majorName = in.readString();
    }

    public static final Creator<DragonMajor> CREATOR = new Creator<DragonMajor>() {
        @Override
        public DragonMajor createFromParcel(Parcel in) {
            return new DragonMajor(in);
        }

        @Override
        public DragonMajor[] newArray(int size) {
            return new DragonMajor[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(majorName);
    }

    @Override
    public String toString() {
        return majorName;
    }
}
