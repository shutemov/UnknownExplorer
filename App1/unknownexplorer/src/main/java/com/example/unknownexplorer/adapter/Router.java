package com.example.unknownexplorer.adapter;

import android.content.res.Resources;
import android.util.Log;

import com.example.unknownexplorer.R;

public class Router {
    String title;
    String description;
    String interest;
    String type;
    String time;

    int pic =  R.drawable.ic_launcher_foreground;

    public Router(String title, String description, String interest, String type, String time, String rating) {
        Log.d("test", "Router: hello");
        this.title = title;
        this.description = description;
        this.interest = interest;
        this.type = type;
        this.time = time;
        this.rating = rating;
    }
    public int getPic() {
        return pic;
    }
    public String getInterest() {
        return interest;
    }

    public String getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

    public String getRating() {
        return rating;
    }

    String rating;



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
