package com.tw.techradar.ui.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import com.tw.techradar.model.RadarItem;

import java.util.StringTokenizer;

public abstract class Blip {
    protected static final int BLIP_COLOR = Color.rgb(37, 170, 225);
    private static final String TRIANGLE_BLIP_SYMBOL = "t";
    protected static final float TEXT_SIZE = 10;

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

    public abstract void render(Canvas canvas, int currentQuadrant);

    public double getDistanceFromPoint(float x, float y){
        return Math.sqrt(Math.pow(getXCoordinate() - x, 2) +  Math.pow(getYCoordinate() - y,2));
    }

    protected void renderBlipTitlesIfQuadrantView(Canvas canvas, int currentView, Paint paint) {
        if (isQuadrantView(currentView)) {
            TextPaint textPaint = new TextPaint(paint);
            textPaint.setColor(Color.BLACK);
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setTextSize(TEXT_SIZE);
            StaticLayout layout = new StaticLayout(getRadarItem().getName(), textPaint, getTextWidth(radarItem.getName(), paint), Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
            canvas.translate(this.getXCoordinate() - radius, this.yCoordinate + radius); //position the text
            layout.draw(canvas);
            canvas.translate(-(this.getXCoordinate() - radius), -(this.yCoordinate + radius)); //position the text
        }
    }

    private int getTextWidth(String text, Paint paint){
        int width = 0;
        StringTokenizer stringTokenizer = new StringTokenizer(text);
        while(stringTokenizer.hasMoreTokens()){
            String token = stringTokenizer.nextToken();
            int textWidth = getTextBounds(token, paint).width();
            if (textWidth > width){
                width =  textWidth;
            }
        }
        return width;
    }

    private boolean isQuadrantView(int currentQuadrant) {
        return currentQuadrant != 0;
    }

    private Rect getTextBounds(String text, Paint textPaint) {
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds;
    }

}
