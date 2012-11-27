package com.tw.techradar.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class PointCoordinates implements Serializable {

    @JsonProperty("r")
    private int radius;

    @JsonProperty("t")
    private int theta;

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getTheta() {
        return theta;
    }

    public void setTheta(int theta) {
        this.theta = theta;
    }
}
