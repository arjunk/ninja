package com.tw.techradar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RadarItem implements Serializable {

    @JsonProperty("tip")
    private String tip;

    @JsonProperty("name")
    private String name;

    @JsonProperty("movement")
    private String movement;

    @JsonProperty("description")
    private String description;

    @JsonProperty("pc")
    private PointCoordinates pc;

    public static final String ITEM_KEY = "ITEM_INFO_KEY";
    private int quadrant;

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMovement() {
        return movement;
    }

    public void setMovement(String movement) {
        this.movement = movement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRadius() {
        return pc.getRadius();
    }

    public int getTheta() {
        return pc.getTheta();
    }

    public int getQuadrant() {
        return this.quadrant;
    }

    private int determineQuadrant(){
        if (getTheta() <= 90){
            return 1;
        }else if (getTheta() <=180){
            return 2;
        }else if (getTheta() <=270){
            return 3;
        }else
            return 4;
    }

    public PointCoordinates getPc() {
        return pc;
    }

    public void setPc(PointCoordinates pc) {
        this.pc = pc;
        this.quadrant = determineQuadrant();
    }

    public void setTheta(int theta) {
        this.pc.setTheta(theta);
    }

    public void setRadius(int radius) {
        this.pc.setRadius(radius);
    }
}
