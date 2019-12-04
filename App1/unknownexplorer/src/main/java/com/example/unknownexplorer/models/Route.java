package com.example.unknownexplorer.models;

import android.util.Log;

import com.example.unknownexplorer.R;

public class Route {
    long id;
    String title;
    String description;
    String interest;
    String type;
    String time;
    String rating;

    int pic =  R.drawable.ic_launcher_foreground;


    public Route(long id, String title, String description, String interest, String type, String time, String rating) {
        this.id = id;
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


    public long getId() {
        return id;
    }



    public String getTitle() {
        Log.d("test","getTitle from Route");
        Log.d("test","VALUE TITLE " + title);
        return title;
    }

    public String getDescription() {
        Log.d("test","getDescription from Route");
        Log.d("test","VALUE DESCR " + description);
        return description;
    }
}
