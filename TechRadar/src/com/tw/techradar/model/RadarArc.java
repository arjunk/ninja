package com.tw.techradar.model;

import java.io.Serializable;

public class RadarArc implements Serializable{
    int radius;
    String name;

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
