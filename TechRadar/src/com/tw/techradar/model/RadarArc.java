package com.tw.techradar.model;

import java.io.Serializable;

public class RadarArc implements Serializable{
    private int radius;
    private String name;
    private int startOffset;

    public RadarArc(int radius, String name, int startOffset) {
        this.radius = radius;
        this.name = name;
        this.startOffset = startOffset;
    }

    public int getRadius() {
        return radius;
    }

    public String getName() {
        return name;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public boolean isRadarItemInArc(RadarItem radarItem){
        return (radarItem.getRadius() >= this.startOffset) && (radarItem.getRadius() <= this.radius);
    }
}
