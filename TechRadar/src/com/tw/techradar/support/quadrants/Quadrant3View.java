package com.tw.techradar.support.quadrants;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;
import com.tw.techradar.constants.SizeConstants;
import com.tw.techradar.model.Radar;

public class Quadrant3View extends  QuadrantView{
    private int startX;
    private int endX;
    private int startY;
    private int endY;

    public Quadrant3View(DisplayMetrics displayMetrics, View mainView, Radar radarData, int marginX, int marginY) {
        super(displayMetrics, mainView, radarData, marginX, marginY);
        this.startX = 0;
        this.endX =displayMetrics.widthPixels / 2;
        this.startY = displayMetrics.heightPixels / 2;
        this.endY = displayMetrics.heightPixels;
    }
    @Override
    protected int getEndY() {
        return this.endY;
    }

    @Override
    protected int getStartY() {
        return this.startY;
    }

    @Override
    protected int getEndX() {
        return this.endX;
    }

    @Override
    protected int getStartX() {
        return this.startX;
    }

    @Override
    public int getQuadrantNo() {
        return 3;
    }

    @Override
    protected String getQuadrantName() {
        return radarData.getQuadrants().get(2).getName();
    }

    @Override
    protected void renderQuadrantCaption(Canvas canvas) {
        canvas.drawText(getQuadrantName(), SizeConstants.MARGIN_PADDING_PIXELS, SizeConstants.MARGIN_PADDING_PIXELS, getQuadrantTextPaint(Paint.Align.LEFT, SizeConstants.ZOOMED_QUADRANT_TEXT_SIZE));
    }

    @Override
    protected void determineMaxRadiusAndOrigins() {
        screenOriginY = 0;
        screenOriginX = -displayMetrics.widthPixels;
        maxRadius = displayMetrics.widthPixels - SizeConstants.RADIUS_MARGIN_PIXELS;
    }
}
