package com.example.unknownexplorer.models;

public class Point {

    long id;
    String name;
    String XCoord;
    String YCoord;

    public int getId() {
        return (int) id;
    }

    public String getName() {
        return name;
    }

    public String getXCoord() {
        return XCoord;
    }

    public String getYCoord() {
        return YCoord;
    }


    public Point(long id, String name, String XCoord, String YCoord) {
        this.id = id;
        this.name = name;
        this.XCoord = XCoord;
        this.YCoord = YCoord;
    }
}
