package com.tw.techradar.activity;

import android.graphics.Canvas;

class Blip {
    private float xCoordinate;
    private float yCoordinate;
    private BlipType blipType;


    public float getXCoordinate() {
        return xCoordinate;
    }

    public float getYCoordinate() {
        return yCoordinate;
    }

    public BlipType getBlipType() {
        return blipType;
    }

    Blip(float xCoordinate, float yCoordinate, BlipType blipType) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.blipType = blipType;
    }

    void drawOn(Canvas canvas) {
        getBlipType().drawOn(canvas, this);
    }
}
