package com.example.unknownexplorer.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PojoPoint {
    @SerializedName("id")
    @Expose
    int id;

    @SerializedName("route_way_id")
    @Expose
    int routeId;

    @SerializedName("title")
    @Expose
    String title;

    @SerializedName("xCoord")
    @Expose
    String XCoord;

    @SerializedName("yCoord")
    @Expose
    String YCoord;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getXCoord() {
        return XCoord;
    }

    public void setXCoord(String XCoord) {
        this.XCoord = XCoord;
    }

    public String getYCoord() {
        return YCoord;
    }

    public void setYCoord(String YCoord) {
        this.YCoord = YCoord;
    }
}
