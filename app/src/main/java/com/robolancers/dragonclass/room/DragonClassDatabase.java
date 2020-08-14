package com.robolancers.dragonclass.room;

import android.content.Context;
import android.util.Log;

import com.robolancers.dragonclass.room.daos.DragonClassDao;
import com.robolancers.dragonclass.room.entities.DragonClass;
import com.robolancers.dragonclass.utilities.AppExecutors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.Executors;

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

                    for (Element course : courseBlocks) {
                        Element courseBlockTitleElement = course.getElementsByClass("courseblocktitle").first();
                        Element courseBlockDescriptionElement = course.getElementsByClass("courseblockdesc").first();

                        String[] courseBlockTitleSplit = courseBlockTitleElement.text().split(" ");

                        String courseText = course.text();
                        String[] courseTextSplit = courseText.split("Prerequisites:");

                        String prerequisites = courseTextSplit.length == 2 ? courseTextSplit[1] : "N/A";

                        DragonClass dragonClass = new DragonClass(
                                courseBlockTitleSplit[0] + courseBlockTitleSplit[1],
                                courseBlockDescriptionElement.text(),
                                prerequisites
                        );

                        instance.dragonClassDao().insert(dragonClass);
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
