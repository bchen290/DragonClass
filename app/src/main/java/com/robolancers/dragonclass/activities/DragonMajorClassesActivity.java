package com.robolancers.dragonclass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.robolancers.dragonclass.R;
import com.robolancers.dragonclass.adapters.DragonClassAdapter;
import com.robolancers.dragonclass.room.DragonClassDatabase;
import com.robolancers.dragonclass.room.entities.DragonMajor;
import com.robolancers.dragonclass.room.viewmodels.DragonClassViewModel;

public class DragonMajorClassesActivity extends AppCompatActivity {
    private DragonClassViewModel dragonClassViewModel;
    private RecyclerView recyclerView;
    private DragonClassAdapter dragonClassAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DragonMajor dragonMajor = getIntent().getParcelableExtra("DragonMajor");

        recyclerView =  findViewById(R.id.class_recycler_view);
        dragonClassAdapter = new DragonClassAdapter(this);

        recyclerView.setAdapter(dragonClassAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DragonClassDatabase.getDatabase(this).dragonClassDao().getClassesWithMajor(dragonMajor.getMajorName()).observe(this, dragonClasses -> dragonClassAdapter.setClasses(dragonClasses));
    }
}