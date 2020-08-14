package com.robolancers.dragonclass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.robolancers.dragonclass.adapters.DragonClassAdapter;
import com.robolancers.dragonclass.room.viewmodels.DragonClassViewModel;

public class MainActivity extends AppCompatActivity {
    private DragonClassViewModel dragonClassViewModel;
    private RecyclerView recyclerView;
    private DragonClassAdapter dragonClassAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView =  findViewById(R.id.class_recycler_view);
        dragonClassAdapter = new DragonClassAdapter(this);

        recyclerView.setAdapter(dragonClassAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dragonClassViewModel = ViewModelProviders.of(this).get(DragonClassViewModel.class);
        dragonClassViewModel.getAllClasses().observe(this, dragonClasses -> {
            dragonClassAdapter.setClasses(dragonClasses);
        });
    }
}