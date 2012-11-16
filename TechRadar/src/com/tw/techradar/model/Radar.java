package com.tw.techradar.model;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

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

    public List<RadarItem> getItemWithText(List<RadarItem> radarItems, final CharSequence text){
        return new ArrayList<RadarItem>(Collections2.filter(radarItems, new Predicate<RadarItem>() {
            @Override
            public boolean apply(RadarItem radarItem) {
                if(radarItem.getDescription().contains(text) || radarItem.getName().contains(text)){
                    return true;
                }
                return false;
            }
        }));
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

    public RadarArc getRadarArc(String name) {
        for (RadarArc radarArc : getRadarArcs()) {
            if(radarArc.getName().equals(name)){
                 return radarArc;
            }
        }
        return null;
    }
}
