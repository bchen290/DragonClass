package com.robolancers.dragonclass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

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
        setContentView(R.layout.activity_dragon_major_classes);

        DragonMajor dragonMajor = getIntent().getParcelableExtra("DragonMajor");

        recyclerView =  findViewById(R.id.class_recycler_view);
        dragonClassAdapter = new DragonClassAdapter(this);

        recyclerView.setAdapter(dragonClassAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DragonClassDatabase.getDatabase(this).dragonClassDao().getClassesWithMajor(dragonMajor.getMajorName()).observe(this, dragonClasses -> dragonClassAdapter.setClasses(dragonClasses));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                dragonClassAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                dragonClassAdapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }
}