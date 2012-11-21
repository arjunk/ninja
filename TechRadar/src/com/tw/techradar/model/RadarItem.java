package com.tw.techradar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RadarItem implements Serializable {

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
        return pc.getR();
    }

    public int getTheta() {
        return pc.getT();
    }

    private String tip;
    private String name;
    private String movement;
    private String description;
    private PC pc;


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

    public PC getPc() {
        return pc;
    }

    public void setPc(PC pc) {
        this.pc = pc;
        this.quadrant = determineQuadrant();
    }

    public void setTheta(int theta) {
        this.pc.setT(theta);
    }

    public void setRadius(int radius) {
        this.pc.setR(radius);
    }
}
