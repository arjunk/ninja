package com.tw.techradar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RadarArc implements Serializable{

    @JsonProperty("r")
    private int radius;

    @JsonProperty("name")
    private String name;

    private int startOffset;

    public boolean isRadarItemInArc(RadarItem radarItem){
        return radarItem.getRadius() <= getRadius() && radarItem.getRadius() >= startOffset;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }
}
