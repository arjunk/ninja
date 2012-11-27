package com.tw.techradar.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class RadarQuadrant implements Serializable{

    @JsonProperty("tip")
    private String tip;

    @JsonProperty("name")
    private String name;

    @JsonProperty("start")
    private int start;

    @JsonProperty("end")
    private int end;

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

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
