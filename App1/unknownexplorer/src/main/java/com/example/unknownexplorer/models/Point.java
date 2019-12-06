package com.example.unknownexplorer.models;

public class Point {

    long id;
    String name;
    String XCoord;
    String YCoord;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Point(long id, String name, String XCoord, String YCoord) {
        this.id = id;
        this.name = name;
        this.XCoord = XCoord;
        this.YCoord = YCoord;
    }
}
