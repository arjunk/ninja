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
    private Rect dimensionsWithText;
    protected static Paint paint;
    protected static TextPaint textPaint;
    private StaticLayout formattedDescription;
    private boolean frozen;

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
        initGuiObjects();
    }

    private void initGuiObjects() {
        createPaintObject();
        createTextPaintObject();
    }

    private Rect getBlipDimensions() {
        return new Rect((int)this.getXCoordinate() - this.radius,(int) this.getYCoordinate() - this.radius,(int)this.getXCoordinate() - this.radius + formattedDescription.getWidth(),(int) this.getYCoordinate() + this.radius + formattedDescription.getHeight());
    }

    private StaticLayout getFormattedDescription(RadarItem radarItem) {
        return new StaticLayout(getRadarItem().getName(), textPaint, getLineWidth(radarItem.getName()), Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
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

    protected void renderBlipTitlesIfQuadrantView(Canvas canvas, int currentView) {
        if (isQuadrantView(currentView)) {
            canvas.translate(this.getXCoordinate() - radius, this.yCoordinate + radius); //position the text
            determineDimensionsWithText();
            formattedDescription.draw(canvas);
            canvas.translate(-(this.getXCoordinate() - radius), -(this.yCoordinate + radius)); //position the text
        }
    }

    private  static void createTextPaintObject() {
        if (textPaint!=null) return;
        textPaint = new TextPaint(paint);
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(TEXT_SIZE);
    }

    private int getLineWidth(String text){
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

    private void determineDimensionsWithText(){
        if (this.formattedDescription==null)
            formattedDescription = getFormattedDescription(radarItem);

        this.dimensionsWithText = getBlipDimensions();
    }


    public Rect getDimensionsWithText() {
            determineDimensionsWithText();
        return dimensionsWithText;
    }

    public void shiftCoOrdinates(float x, float y){
        this.xCoordinate  = x;
        this.yCoordinate = y;
    }

    private  static void createPaintObject() {
        if (paint != null) return;
        Paint paintObj = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintObj.setStrokeWidth(2);
        paintObj.setColor(BLIP_COLOR);
        paintObj.setStyle(Paint.Style.FILL_AND_STROKE);
        paintObj.setAntiAlias(true);
        paint = paintObj;
    }

    public void freeze() {
        this.frozen = true;
    }

    public boolean isFrozen(){
        return this.frozen;
    }
}
