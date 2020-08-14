package com.robolancers.dragonclass.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.robolancers.dragonclass.R;
import com.robolancers.dragonclass.room.entities.DragonClass;
import com.robolancers.dragonclass.utilities.Utility;

import java.util.Objects;

public class DragonClassDetailActivity extends AppCompatActivity {
    private TextView courseID, courseDescription, courseDependencies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dragon_class_detail);

        courseID = findViewById(R.id.course_id);
        courseDescription = findViewById(R.id.course_description);
        courseDependencies = findViewById(R.id.course_dependencies);

        DragonClass dragonClass = getIntent().getParcelableExtra("DragonClass");

        if (dragonClass != null) {
            courseID.setText(dragonClass.getCourseID());
            courseDescription.setText(dragonClass.getCourseDescription());
            courseDependencies.setText("Dependencies: " + Objects.requireNonNull(Utility.getInstance().dependencies.get(dragonClass.getCourseID())).toString());
        }
    }
}