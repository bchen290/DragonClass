package com.robolancers.dragonclass.utilities;

import java.util.ArrayList;
import java.util.HashMap;

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
}
