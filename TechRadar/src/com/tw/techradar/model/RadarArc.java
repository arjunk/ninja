package com.tw.techradar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RadarArc implements Serializable{
    private int r;
    private String name;
    private int startOffset;

    public int getStartOffset() {
        return startOffset;
    }

    public int setStartOffset(int offset) {
        return this.startOffset = offset;
    }

    public boolean isRadarItemInArc(RadarItem radarItem){
        return (radarItem.getRadius() >= this.startOffset) && (radarItem.getRadius() <= this.r);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRadius() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }
}
