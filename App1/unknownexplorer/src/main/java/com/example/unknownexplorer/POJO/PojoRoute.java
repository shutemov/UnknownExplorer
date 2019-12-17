package com.example.unknownexplorer.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PojoRoute {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("user_id")
    @Expose
    private int userId;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("interest")
    @Expose
    private String interest;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("points")
    @Expose
    private ArrayList<PojoPoint> points;

    public ArrayList<PojoPoint> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<PojoPoint> points) {
        this.points = points;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}
