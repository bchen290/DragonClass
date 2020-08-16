package com.robolancers.dragonclass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.robolancers.dragonclass.adapters.DragonClassAdapter;
import com.robolancers.dragonclass.room.DragonClassDatabase;
import com.robolancers.dragonclass.room.entities.DragonClass;
import com.robolancers.dragonclass.room.viewmodels.DragonClassViewModel;
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
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private DragonClassViewModel dragonClassViewModel;
    private RecyclerView recyclerView;
    private DragonClassAdapter dragonClassAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppExecutors.getInstance().diskIO().execute(() -> {
            DragonClassDatabase.getDatabase(MainActivity.this).dragonClassDao().deleteAll();

            try {
                Document document = Jsoup.connect("http://catalog.drexel.edu/coursedescriptions/quarter/undergrad/cs/").get();
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
                            prerequisites
                    );

                    DragonClassDatabase.getDatabase(MainActivity.this).dragonClassDao().insert(dragonClass);
                    dependencies.put(dragonClass.getCourseID(), new ArrayList<>());

                    for (String prerequisite : prerequisitesSplit) {
                        String[] prerequisiteSplit = prerequisite.trim().split(" ");

                        if (!prerequisite.equals("N/A")) {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

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