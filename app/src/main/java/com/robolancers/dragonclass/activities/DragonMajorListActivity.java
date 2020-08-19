package com.robolancers.dragonclass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.robolancers.dragonclass.R;
import com.robolancers.dragonclass.adapters.DragonMajorAdapter;
import com.robolancers.dragonclass.room.DragonClassDatabase;
import com.robolancers.dragonclass.room.entities.DragonClass;
import com.robolancers.dragonclass.room.entities.DragonMajor;
import com.robolancers.dragonclass.room.viewmodels.DragonClassViewModel;
import com.robolancers.dragonclass.room.viewmodels.DragonMajorViewModel;
import com.robolancers.dragonclass.utilities.AppExecutors;
import com.robolancers.dragonclass.utilities.Utility;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DragonMajorListActivity extends AppCompatActivity {
    private RecyclerView dragonMajorRecyclerView;
    private DragonMajorAdapter dragonMajorAdapter;

    private DragonMajorViewModel dragonMajorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major_list);

        AppExecutors.getInstance().diskIO().execute(() -> {
            try {
                Document document = Jsoup.connect("http://catalog.drexel.edu/coursedescriptions/quarter/undergrad/")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
                        .maxBodySize(0)
                        .timeout(0)
                        .get();

                Elements links = document.getElementsByAttributeValueStarting("href", "/coursedescriptions/quarter/undergrad/");

                for (Element link : links) {
                    DragonClassDatabase.getDatabase(DragonMajorListActivity.this).dragonMajorDao().insert(new DragonMajor(link.text()));

                    document = Jsoup.connect("http://catalog.drexel.edu" + link.attr("href")).get();
                    Elements courseBlocks = document.getElementsByClass("courseblock");
                    HashMap<String, ArrayList<String>> dependencies = Utility.getInstance().dependencies;

                    for (Element course : courseBlocks) {
                        Element courseBlockTitleElement = course.getElementsByClass("courseblocktitle").first();
                        Element courseBlockDescriptionElement = course.getElementsByClass("courseblockdesc").first();

                        String[] courseBlockTitleSplit = courseBlockTitleElement.text().split(" ");

                        String title = Arrays.stream(courseBlockTitleSplit, 2, courseBlockTitleSplit.length - 2).reduce("", (subtotal, element) -> subtotal + " " + element);

                        String courseText = course.text();
                        String[] courseTextSplit = courseText.split("Prerequisites:");

                        String prerequisites = courseTextSplit.length == 2 ? courseTextSplit[1] : "N/A";
                        String[] prerequisitesSplit = prerequisites.split("and|or");

                        DragonClass dragonClass = new DragonClass(
                                courseBlockTitleSplit[0] + courseBlockTitleSplit[1],
                                title,
                                courseBlockDescriptionElement.text(),
                                prerequisites,
                                link.text()
                        );

                        DragonClassDatabase.getDatabase(DragonMajorListActivity.this).dragonClassDao().insert(dragonClass);
                        dependencies.put(dragonClass.getCourseID(), new ArrayList<>());

                        for (String prerequisite : prerequisitesSplit) {
                            String[] prerequisiteSplit = prerequisite.trim().split(" ");

                            if (!prerequisite.equals("N/A") && !prerequisite.trim().isEmpty()) {
                                String prerequisiteCourseID = (prerequisiteSplit[0].replaceAll("\\(", "") + prerequisiteSplit[1]);

                                if (dependencies.containsKey(prerequisiteCourseID)) {
                                    if (!Objects.requireNonNull(dependencies.getOrDefault(prerequisiteCourseID, new ArrayList<>())).contains(dragonClass.getCourseID().replace("\\s", ""))) {
                                        dependencies.computeIfPresent(prerequisiteCourseID, (k, v) -> {
                                            v.add(dragonClass.getCourseID());
                                            return v;
                                        });
                                    }
                                } else {
                                    dependencies.put(prerequisiteCourseID, new ArrayList<>(Collections.singletonList(dragonClass.getCourseID().replace("\\s", ""))));
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        dragonMajorAdapter = new DragonMajorAdapter(this);
        dragonMajorRecyclerView = findViewById(R.id.dragon_major_recyclerview);

        dragonMajorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dragonMajorRecyclerView.setAdapter(dragonMajorAdapter);

        dragonMajorViewModel = ViewModelProviders.of(this).get(DragonMajorViewModel.class);
        dragonMajorViewModel.getAllMajors().observe(this, dragonMajors -> dragonMajorAdapter.setDragonMajors(dragonMajors));
    }
}