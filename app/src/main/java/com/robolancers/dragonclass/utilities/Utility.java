package com.robolancers.dragonclass.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.robolancers.dragonclass.R;
import com.robolancers.dragonclass.room.DragonClassDatabase;
import com.robolancers.dragonclass.room.entities.DragonClass;
import com.robolancers.dragonclass.room.entities.DragonMajor;

import org.jsoup.Connection;
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
import java.util.Map;
import java.util.Objects;

import androidx.recyclerview.widget.RecyclerView;

public class Utility {
    public static void downloadMajorsAndClasses(Activity activity, TextView progressText, RecyclerView dragonMajorRecyclerView, LinearLayout progressHolder) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(activity.getString(R.string.app_name), Context.MODE_PRIVATE);

        if (!sharedPreferences.getBoolean("hasDownloaded", false)) {
            AppExecutors.getInstance().diskIO().execute(() -> {
                try {
                    activity.runOnUiThread(() -> progressText.setText("Downloading courses"));

                    Connection.Response response = Jsoup.connect("http://catalog.drexel.edu/coursedescriptions/quarter/undergrad/")
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36")
                            .referrer("http://www.google.com")
                            .maxBodySize(0)
                            .timeout(0)
                            .followRedirects(true)
                            .execute();

                    if (response.statusCode() < 400) {
                        Document document = response.parse();
                        Elements links = document.getElementsByAttributeValueStarting("href", "/coursedescriptions/quarter/undergrad/");
                        HashMap<String, List<String>> dependencies = new HashMap<>();

                        for (Element link : links) {
                            DragonClassDatabase.getDatabase(activity).dragonMajorDao().insert(new DragonMajor(link.text()));
                            activity.runOnUiThread(() -> progressText.setText("Downloading " + link.text() + "'s classes"));

                            document = Jsoup.connect("http://catalog.drexel.edu" + link.attr("href")).get();
                            Elements courseBlocks = document.getElementsByClass("courseblock");

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
                                        link.text(),
                                        ""
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

                        Gson gson = new Gson();

                        for (Map.Entry<String, List<String>> dependency : dependencies.entrySet()) {
                            DragonClass dragonClass = DragonClassDatabase.getDatabase(activity).dragonClassDao().getClassByCourseID(dependency.getKey()).getValue().get(0);
                            dragonClass.setDependencies(gson.toJson(dependency.getValue()));
                            DragonClassDatabase.getDatabase(activity).dragonClassDao().insert(dragonClass);
                        }

                        activity.runOnUiThread(() -> {
                            dragonMajorRecyclerView.setVisibility(View.VISIBLE);
                            progressHolder.setVisibility(View.GONE);

                            sharedPreferences.edit().putBoolean("hasDownloaded", true).apply();
                        });
                    } else {
                        progressText.setText(response.statusMessage());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
