package com.example.unknownexplorer.adapter;

import android.util.Log;

public class Router {
    String title;
    String description;

    public Router(String title, String description) {
        Log.d("test","Router from Router");
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        Log.d("test","getTitle from Router");
        Log.d("test","VALUE TITLE " + title);
        return title;
    }

    public String getDescription() {
        Log.d("test","getDescription from Router");
        Log.d("test","VALUE DESCR " + description);
        return description;
    }
}
