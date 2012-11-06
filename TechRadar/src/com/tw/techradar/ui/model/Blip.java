package com.tw.techradar.ui.model;

import android.graphics.Canvas;
import android.graphics.Color;
import com.tw.techradar.model.RadarItem;

public abstract class Blip {
    protected static final int BLIP_COLOR = Color.rgb(37, 170, 225);
    private static final String TRIANGLE_BLIP_SYMBOL = "t";

    protected float xCoordinate;
    protected float yCoordinate;
    protected RadarItem radarItem;
    protected int radius = 0;


    public float getXCoordinate() {
        return xCoordinate;
    }

    public float getYCoordinate() {
        return yCoordinate;
    }

    public Blip(float xCoordinate, float yCoordinate, RadarItem radarItem) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.radarItem = radarItem;
    }


    public boolean isPointInBlip(float x, float y){
        double d = Math.pow(xCoordinate - x , 2) + Math.pow(yCoordinate - y, 2);
        return  d<= radius * radius;
    }

    public RadarItem getRadarItem() {
        return radarItem;
    }

    public static Blip getBlipForRadarItem(RadarItem radarItem, float xCordinate, float yCordinate, float displayDensityDPI){
        if (radarItem.getMovement().equals(TRIANGLE_BLIP_SYMBOL))
            return new TriangleBlip(xCordinate, yCordinate, radarItem, displayDensityDPI);
        else
            return new CircleBlip(xCordinate,yCordinate,radarItem, displayDensityDPI);
    }

    public abstract int getRadius();

    public abstract void render(Canvas canvas);

    public double getDistanceFromPoint(float x, float y){
        return Math.sqrt(Math.pow(getXCoordinate() - x, 2) +  Math.pow(getYCoordinate() - y,2));
    }
}
