package com.robolancers.dragonclass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.robolancers.dragonclass.R;
import com.robolancers.dragonclass.adapters.DependencyAdapter;
import com.robolancers.dragonclass.room.entities.DragonClass;
import com.robolancers.dragonclass.utilities.Utility;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DragonClassDetailActivity extends AppCompatActivity {
    private TextView courseID, courseDescription;

    private RecyclerView courseDependencies;
    private DependencyAdapter dependencyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dragon_class_detail);

        courseID = findViewById(R.id.course_id);
        courseDescription = findViewById(R.id.course_description);

        courseDependencies = findViewById(R.id.course_dependencies);
        dependencyAdapter = new DependencyAdapter(this);

        courseDependencies.setAdapter(dependencyAdapter);
        courseDependencies.setLayoutManager(new LinearLayoutManager(this));

        DragonClass dragonClass = getIntent().getParcelableExtra("DragonClass");

        if (dragonClass != null) {
            courseID.setText(dragonClass.getCourseID());
            courseDescription.setText(dragonClass.getCourseDescription());

            List<String> courses = Utility.getInstance().dependencies.get(dragonClass.getCourseID());

            if (courses != null && !courses.isEmpty()) {
                dependencyAdapter.setCourseIDs(courses);
            } else {
                dependencyAdapter.setCourseIDs(Collections.singletonList("This course has no dependencies"));
            }
        }
    }
}