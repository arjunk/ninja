package com.tw.techradar.support.quadrants;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;
import com.tw.techradar.constants.SizeConstants;
import com.tw.techradar.model.Radar;
import com.tw.techradar.ui.model.Blip;

public class Quadrant4View extends QuadrantView{
    private int startX;
    private int endX;
    private int startY;
    private int endY;

    public Quadrant4View(DisplayMetrics displayMetrics, View mainView, Radar radarData, int marginX, int marginY) {
        super(displayMetrics, mainView, radarData, marginX, marginY);
        this.startX = displayMetrics.widthPixels / 2;
        this.endX = displayMetrics.widthPixels;
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
    public QuadrantType getQuadrantType() {
        return QuadrantType.QUADRANT_4;
    }

    @Override
    protected String getQuadrantName() {
        return radarData.getQuadrants().get(3).getName();
    }

    @Override
    protected void renderQuadrantCaption(Canvas canvas) {
        canvas.drawText(getQuadrantName(), marginFromRight, marginFromBottom, getQuadrantTextPaint(Paint.Align.RIGHT, SizeConstants.ZOOMED_QUADRANT_TEXT_SIZE));
    }

    @Override
    protected void determineMaxRadiusAndOrigins() {
        screenOriginY = 0;
        screenOriginX = 0;
        maxRadius = displayMetrics.widthPixels - SizeConstants.RADIUS_MARGIN_PIXELS;
    }

    @Override
    protected int getQuadrantStartTheta() {
        return 270;
    }

    @Override
    protected int getQuadrantEndTheta() {
        return 360;
    }

    @Override
    protected int getThetaAdjustmentForOverlap(Blip blip) {
        return (blip.getDimensionsWithText().left < 0) ? 1 : -1;
    }

}
