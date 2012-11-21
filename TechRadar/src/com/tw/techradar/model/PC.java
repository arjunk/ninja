package com.tw.techradar.model;

import java.io.Serializable;

public class PC implements Serializable {
    private int r;
    private int t;

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }
}
