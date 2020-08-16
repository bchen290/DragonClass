package com.robolancers.dragonclass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.robolancers.dragonclass.MainActivity;
import com.robolancers.dragonclass.R;
import com.robolancers.dragonclass.adapters.DragonMajorAdapter;
import com.robolancers.dragonclass.room.DragonClassDatabase;
import com.robolancers.dragonclass.room.entities.DragonClass;
import com.robolancers.dragonclass.utilities.AppExecutors;
import com.robolancers.dragonclass.utilities.Utility;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DragonMajorListActivity extends AppCompatActivity {
    private RecyclerView dragonMajorRecyclerView;
    private DragonMajorAdapter dragonMajorAdapter;

    private List<String> majors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major_list);

        majors = new ArrayList<>();

        AppExecutors.getInstance().diskIO().execute(() -> {
            try {
                Document document = Jsoup.connect("http://catalog.drexel.edu/coursedescriptions/quarter/undergrad/").get();

                Element column1 = document.getElementById("listCol1");
                Elements links = column1.select(":root > div > a");

                for (Element link : links) {
                    majors.add(link.text());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        dragonMajorAdapter = new DragonMajorAdapter(this);
        dragonMajorRecyclerView = findViewById(R.id.dragon_major_recyclerview);

        dragonMajorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dragonMajorRecyclerView.setAdapter(dragonMajorAdapter);

        dragonMajorAdapter.setDragonMajors(majors);
    }
}