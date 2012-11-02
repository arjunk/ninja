package com.tw.techradar.activity;

import android.graphics.Canvas;
import com.tw.techradar.model.RadarItem;

class Blip {
    private float xCoordinate;
    private float yCoordinate;
    private BlipType blipType;
    private RadarItem radarItem;


    public float getXCoordinate() {
        return xCoordinate;
    }

    public float getYCoordinate() {
        return yCoordinate;
    }

    public BlipType getBlipType() {
        return blipType;
    }

    Blip(float xCoordinate, float yCoordinate, BlipType blipType, RadarItem radarItem) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.blipType = blipType;
        this.radarItem = radarItem;
    }

    void drawOn(Canvas canvas) {
        ((DrawableBlip)getBlipType()).drawOn(canvas, this);
    }

    public RadarItem getRadarItem() {
        return radarItem;
    }
}
