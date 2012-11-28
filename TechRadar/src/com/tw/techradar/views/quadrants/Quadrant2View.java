package com.tw.techradar.views.quadrants;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.DisplayMetrics;
import android.view.View;
import com.tw.techradar.constants.SizeConstants;
import com.tw.techradar.model.Radar;
import com.tw.techradar.views.model.Blip;

public class Quadrant2View extends QuadrantView{
    private int startX;
    private int endX;
    private int startY;
    private int endY;

    public Quadrant2View(DisplayMetrics displayMetrics, View mainView, Radar radarData) {
        super(displayMetrics, mainView, radarData);
        this.startX = 0;
        this.endX = displayMetrics.widthPixels / 2;
        this.startY = 0;
        this.endY = displayMetrics.heightPixels / 2;
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
        return QuadrantType.QUADRANT_2;
    }

    @Override
    protected String getQuadrantName() {
        return radarData.getQuadrants().get(0).getName();
    }

    @Override
    protected void renderQuadrantCaption(Canvas canvas) {
        canvas.drawText(getQuadrantName(), SizeConstants.MARGIN_PADDING_PIXELS, SizeConstants.MARGIN_PADDING_PIXELS, getQuadrantTextPaint(Paint.Align.LEFT, SizeConstants.ZOOMED_QUADRANT_TEXT_SIZE));
    }

    @Override
    protected void determineMaxRadiusAndOrigins() {
        screenOriginY = displayMetrics.heightPixels;
        screenOriginX = -displayMetrics.widthPixels;
        maxRadius = displayMetrics.widthPixels - SizeConstants.RADIUS_MARGIN_PIXELS;
    }

    @Override
    protected int getQuadrantStartTheta() {
        return 90;
    }

    @Override
    protected int getQuadrantEndTheta() {
        return 180;
    }

    @Override
    protected int getThetaAdjustmentForOverlap(Blip blip) {
        return (blip.getDimensionsWithText().right > displayMetrics.widthPixels) ? 1 : -1;
    }

    @Override
    protected Path.Direction getPathDirection() {
        return Path.Direction.CW;
    }

    @Override
    protected float getOffsetFraction() {
        return 0.25f;
    }

}
