package com.robolancers.dragonclass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.robolancers.dragonclass.R;
import com.robolancers.dragonclass.adapters.DragonMajorAdapter;
import com.robolancers.dragonclass.room.viewmodels.DragonMajorViewModel;
import com.robolancers.dragonclass.utilities.Utility;

public class DragonMajorListActivity extends AppCompatActivity {
    private RecyclerView dragonMajorRecyclerView;
    private DragonMajorAdapter dragonMajorAdapter;

    private DragonMajorViewModel dragonMajorViewModel;

    private LinearLayout progressHolder;
    private TextView progressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major_list);

        progressText = findViewById(R.id.progress_text);

        dragonMajorRecyclerView = findViewById(R.id.dragon_major_recyclerview);
        progressHolder = findViewById(R.id.progress_holder);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

        if (!sharedPreferences.getBoolean("hasDownloaded", false)) {
            dragonMajorRecyclerView.setVisibility(View.GONE);
            progressHolder.setVisibility(View.VISIBLE);
            Utility.downloadMajorsAndClasses(this, progressText, dragonMajorRecyclerView, progressHolder);
        }

        dragonMajorAdapter = new DragonMajorAdapter(this);

        dragonMajorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dragonMajorRecyclerView.setAdapter(dragonMajorAdapter);

        dragonMajorViewModel = ViewModelProviders.of(this).get(DragonMajorViewModel.class);
        dragonMajorViewModel.getAllMajors().observe(this, dragonMajors -> dragonMajorAdapter.setDragonMajors(dragonMajors));
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
                dragonMajorAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                dragonMajorAdapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}