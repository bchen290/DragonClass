package com.robolancers.dragonclass.utilities;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robolancers.dragonclass.activities.DragonMajorListActivity;
import com.robolancers.dragonclass.room.DragonClassDatabase;
import com.robolancers.dragonclass.room.entities.DragonClass;
import com.robolancers.dragonclass.room.entities.DragonMajor;

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

import androidx.recyclerview.widget.RecyclerView;

public class Utility {
    public HashMap<String, ArrayList<String>> dependencies;

    public Utility() {
        dependencies = new HashMap<>();
    }

    private static Utility instance;

    public static Utility getInstance() {
        if(instance == null) {
            synchronized (Utility.class) {
                // Check again after synchronization
                if(instance == null) {
                    instance = new Utility();
                }
            }
        }

        return instance;
    }

    public void downloadMajorsAndClasses(Activity activity, TextView progressText, RecyclerView dragonMajorRecyclerView, LinearLayout progressHolder) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            try {
                activity.runOnUiThread(() -> progressText.setText("Downloading courses"));

                Document document = Jsoup.connect("http://catalog.drexel.edu/coursedescriptions/quarter/undergrad/")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
                        .maxBodySize(0)
                        .timeout(0)
                        .get();

                Elements links = document.getElementsByAttributeValueStarting("href", "/coursedescriptions/quarter/undergrad/");

                for (Element link : links) {
                    DragonClassDatabase.getDatabase(activity).dragonMajorDao().insert(new DragonMajor(link.text()));
                    activity.runOnUiThread(() -> progressText.setText("Downloading " + link.text() + "'s classes"));

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

                        DragonClassDatabase.getDatabase(activity).dragonClassDao().insert(dragonClass);
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

                activity.runOnUiThread(() -> {
                    dragonMajorRecyclerView.setVisibility(View.VISIBLE);
                    progressHolder.setVisibility(View.GONE);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
