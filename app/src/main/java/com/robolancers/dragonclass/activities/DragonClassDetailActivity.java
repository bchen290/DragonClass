package com.robolancers.dragonclass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.robolancers.dragonclass.R;
import com.robolancers.dragonclass.adapters.DependencyAdapter;
import com.robolancers.dragonclass.room.entities.DragonClass;
import com.robolancers.dragonclass.room.entities.DragonMajor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DragonClassDetailActivity extends AppCompatActivity {
    private TextView courseID, courseDescription;

    private RecyclerView courseDependencies;
    private DependencyAdapter dependencyAdapter;

    private DragonClass dragonClass;

    private Gson gson;
    Type type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dragon_class_detail);

        gson = new Gson();
        type = new TypeToken<List<DragonClass>>(){}.getType();

        courseID = findViewById(R.id.course_id);
        courseDescription = findViewById(R.id.course_description);

        courseDependencies = findViewById(R.id.course_dependencies);
        dependencyAdapter = new DependencyAdapter(this);

        courseDependencies.setAdapter(dependencyAdapter);
        courseDependencies.setLayoutManager(new LinearLayoutManager(this));

        dragonClass = getIntent().getParcelableExtra("DragonClass");

        if (dragonClass != null) {
            courseID.setText(dragonClass.getCourseID());
            courseDescription.setText(dragonClass.getCourseDescription());

            List<DragonClass> courses;

            if (!dragonClass.getDependencies().isEmpty()) {
                courses = gson.fromJson(dragonClass.getDependencies(), type);
            } else {
                courses = new ArrayList<>();
            }

            if (courses != null && !courses.isEmpty()) {
                dependencyAdapter.setCourses(courses);
            } else {
                dependencyAdapter.setCourses(Collections.singletonList(new DragonClass("This course has no dependencies","", "","", "", "")));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, DragonMajorClassesActivity.class);
                intent.putExtra("DragonMajor", new DragonMajor(dragonClass.getParentMajorName()));
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}