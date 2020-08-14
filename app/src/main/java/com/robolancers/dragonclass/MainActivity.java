package com.robolancers.dragonclass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.robolancers.dragonclass.utilities.RetrievalCallback;
import com.robolancers.dragonclass.utilities.RetrieveClasses;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements RetrievalCallback {
    TextView testTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testTextView = findViewById(R.id.test);

        RetrieveClasses retrieveClasses = new RetrieveClasses();
        retrieveClasses.retrieveClasses(this);
    }

    @Override
    public void onComplete(final String result) {
        runOnUiThread(() -> testTextView.setText(result));
    }
}