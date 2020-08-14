package com.robolancers.dragonclass.utilities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RetrieveClasses {
    Document document;

    public void retrieveClasses(final RetrievalCallback callback) {
        final StringBuilder result = new StringBuilder();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(() -> {
            try {
                document = Jsoup.connect("http://catalog.drexel.edu/coursedescriptions/quarter/undergrad/cs/").get();
                Elements courseBlocks = document.getElementsByClass("courseblock");

                for (Element course : courseBlocks) {
                    Element courseblocktitle = course.getElementsByClass("courseblocktitle").first();
                    result.append(courseblocktitle.text());
                    result.append("\n");
                }

                callback.onComplete(result.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        executorService.shutdown();
    }
}
