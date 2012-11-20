package com.tw.techradar.support.quadrants;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;
import com.tw.techradar.constants.SizeConstants;
import com.tw.techradar.model.Radar;

public class AllQuadrantView extends QuadrantView {
    public AllQuadrantView(DisplayMetrics displayMetrics, View mainView, Radar radarData, int marginX, int marginY) {
        super(displayMetrics, mainView, radarData, marginX, marginY);
    }

    @Override
    public int getQuadrantNo() {
        return 0;
    }

    @Override
    protected int getEndY() {
        return -1;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected int getStartY() {
        return -1;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected int getEndX() {
        return -1;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected int getStartX() {
        return -1;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected String getQuadrantName() {
        return null;
    }

    @Override
    protected void renderQuadrantCaption(Canvas canvas) {
        String quadrant1Name = radarData.getQuadrants().get(1).getName();
        String quadrant2Name = radarData.getQuadrants().get(0).getName();
        String quadrant3Name = radarData.getQuadrants().get(2).getName();
        String quadrant4Name = radarData.getQuadrants().get(3).getName();

        canvas.drawText(quadrant2Name, SizeConstants.MARGIN_PADDING_PIXELS, SizeConstants.MARGIN_PADDING_PIXELS, getQuadrantTextPaint(Paint.Align.LEFT, SizeConstants.QUADRANT_TEXT_SIZE));
        canvas.drawText(quadrant1Name, marginFromRight, SizeConstants.MARGIN_PADDING_PIXELS, getQuadrantTextPaint(Paint.Align.RIGHT, SizeConstants.QUADRANT_TEXT_SIZE));
        canvas.drawText(quadrant3Name, SizeConstants.MARGIN_PADDING_PIXELS, marginFromBottom, getQuadrantTextPaint(Paint.Align.LEFT, SizeConstants.QUADRANT_TEXT_SIZE));
        canvas.drawText(quadrant4Name, marginFromRight, marginFromBottom, getQuadrantTextPaint(Paint.Align.RIGHT, SizeConstants.QUADRANT_TEXT_SIZE));
    }

    @Override
    protected void determineMaxRadiusAndOrigins() {
        screenOriginY = displayMetrics.heightPixels / 2;
        screenOriginX = -displayMetrics.widthPixels / 2;
        maxRadius = (displayMetrics.widthPixels / 2) - SizeConstants.RADIUS_MARGIN_PIXELS;
    }


}
