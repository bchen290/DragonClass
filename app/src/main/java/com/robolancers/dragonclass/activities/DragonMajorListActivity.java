package com.robolancers.dragonclass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.robolancers.dragonclass.R;
import com.robolancers.dragonclass.adapters.DragonMajorAdapter;
import com.robolancers.dragonclass.utilities.AppExecutors;

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
import java.util.List;
import java.util.stream.Collectors;

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
                Document document = Jsoup.connect("http://catalog.drexel.edu/coursedescriptions/quarter/undergrad/")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
                        .maxBodySize(0)
                        .timeout(0)
                        .get();

                Elements links = document.getElementsByAttributeValueStarting("href", "/coursedescriptions/");

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