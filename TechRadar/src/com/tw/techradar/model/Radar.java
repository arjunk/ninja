package com.tw.techradar.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Radar implements Serializable{

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

    public List<RadarItem> getItemsForArc(RadarArc radarArc){
        List<RadarItem> radarArcItems = new ArrayList<RadarItem>();
        for (RadarItem radarItem : items) {
            if (radarArc.isRadarItemInArc(radarItem)){
                radarArcItems.add(radarItem);
            }
        }
        return radarArcItems;
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
