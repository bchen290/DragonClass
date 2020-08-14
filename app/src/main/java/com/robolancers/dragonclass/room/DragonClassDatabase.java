package com.robolancers.dragonclass.room;

import android.content.Context;
import android.util.Log;

import com.robolancers.dragonclass.room.daos.DragonClassDao;
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
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {DragonClass.class}, version = 1, exportSchema = false)
public abstract class DragonClassDatabase extends RoomDatabase {
    public abstract DragonClassDao dragonClassDao();

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            AppExecutors.getInstance().diskIO().execute(() -> {
                instance.dragonClassDao().deleteAll();

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

                        instance.dragonClassDao().insert(dragonClass);
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
        }
    };

    private static DragonClassDatabase instance;

    public static DragonClassDatabase getDatabase(final Context context) {
        if(instance == null) {
            synchronized (DragonClassDatabase.class) {
                // Check again after synchronization
                if(instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), DragonClassDatabase.class, "dragon_class_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }

        return instance;
    }
}
