package com.tw.techradar.model;

import java.util.List;

public class Radar {

    private List<RadarQuadrant> quadrants;
    private List<RadarItem> items;

    private List<RadarArc> radarArcs;

    private String name;

    public List<RadarQuadrant> getQuadrants() {
        return quadrants;
    }

    public void setQuadrants(List<RadarQuadrant> quadrants) {
        this.quadrants = quadrants;
    }

    public List<RadarItem> getItems() {
        return items;
    }

    public void setItems(List<RadarItem> items) {
        this.items = items;
    }

    public List<RadarArc> getRadarArcs() {
        return radarArcs;
    }

    public void setRadarArcs(List<RadarArc> radarArcs) {
        this.radarArcs = radarArcs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
