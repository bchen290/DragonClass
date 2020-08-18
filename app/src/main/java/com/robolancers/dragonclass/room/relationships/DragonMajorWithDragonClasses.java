package com.robolancers.dragonclass.room.relationships;

import com.robolancers.dragonclass.room.entities.DragonClass;
import com.robolancers.dragonclass.room.entities.DragonMajor;

import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

public class DragonMajorWithDragonClasses {
    @Embedded
    public DragonMajor dragonMajor;

    @Relation(
            parentColumn = "major_name",
            entityColumn = "parent_major_name"
    )
    public List<DragonClass> dragonClasses;
}
