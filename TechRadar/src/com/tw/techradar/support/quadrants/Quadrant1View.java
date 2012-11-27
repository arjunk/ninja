package com.tw.techradar.support.quadrants;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.provider.SyncStateContract;
import android.renderscript.ProgramVertexFixedFunction;
import android.util.DisplayMetrics;
import android.view.View;
import com.tw.techradar.constants.SizeConstants;
import com.tw.techradar.model.Radar;
import com.tw.techradar.ui.model.Blip;

public class Quadrant1View extends QuadrantView{

    private int startX;
    private int endX;
    private int startY;
    private int endY;


    public Quadrant1View(DisplayMetrics displayMetrics, View mainView, Radar radarData, int marginX, int marginY) {
        super(displayMetrics, mainView, radarData, marginX, marginY);
        this.startX = displayMetrics.widthPixels / 2;
        this.endX = displayMetrics.widthPixels;
        this.startY = 0;
        this.endY = displayMetrics.heightPixels / 2;
    }

    @Override
    public QuadrantType getQuadrantType() {
        return QuadrantType.QUADRANT_1;
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
    protected String getQuadrantName() {
        return radarData.getQuadrants().get(1).getName();
    }

    @Override
    protected void renderQuadrantCaption(Canvas canvas) {
        canvas.drawText(getQuadrantName(), marginFromRight, SizeConstants.MARGIN_PADDING_PIXELS, getQuadrantTextPaint(Paint.Align.RIGHT, SizeConstants.ZOOMED_QUADRANT_TEXT_SIZE));
    }

    @Override
    protected void determineMaxRadiusAndOrigins() {
        screenOriginY = displayMetrics.heightPixels;
        screenOriginX = 0;
        maxRadius = displayMetrics.widthPixels - SizeConstants.RADIUS_MARGIN_PIXELS;
    }

    @Override
    protected int getQuadrantStartTheta() {
        return 0;
    }

    @Override
    protected int getQuadrantEndTheta() {
        return 90;
    }

    @Override
    protected int getThetaAdjustmentForOverlap(Blip blip) {
        return (blip.getDimensionsWithText().bottom > displayMetrics.heightPixels) ? 1 : -1 ;
    }

    @Override
    protected Path.Direction getPathDirection() {
        return Path.Direction.CW;
    }

    @Override
    protected float getOffsetFraction() {
        return 0.75f;
    }
}
