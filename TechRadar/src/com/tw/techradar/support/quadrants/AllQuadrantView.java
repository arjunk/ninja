package com.tw.techradar.support.quadrants;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;
import com.tw.techradar.constants.SizeConstants;
import com.tw.techradar.model.Radar;
import com.tw.techradar.ui.model.Blip;

import java.util.ArrayList;

public class AllQuadrantView extends QuadrantView {
    public AllQuadrantView(DisplayMetrics displayMetrics, View mainView, Radar radarData, int marginX, int marginY) {
        super(displayMetrics, mainView, radarData, marginX, marginY);
    }

    @Override
    public QuadrantType getQuadrantType() {
        return QuadrantType.QUADRANT_ALL;
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


    //For quadrant 0 consider all blips to be within the boundary
    @Override
    protected boolean isBlipWithinZoomedQuadrantBoundaries(Blip blip) {
        return true;
    }

    @Override
    protected void renderQuadrantCaption(Canvas canvas) {
        String quadrant1Name = radarData.getQuadrants().get(1).getName();
        String quadrant2Name = radarData.getQuadrants().get(0).getName();
        String quadrant3Name = radarData.getQuadrants().get(2).getName();
        String quadrant4Name = radarData.getQuadrants().get(3).getName();

        quadrantTitleDetails = new ArrayList<QuadrantTitleDetail>();

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(SizeConstants.QUADRANT_TEXT_SIZE);
        paint.setColor(Color.rgb(37, 170, 225));

        quadrantTitleDetails.add(getDetailsForRightAlignedQuadrantTitle(quadrant1Name, paint, marginFromRight, SizeConstants.MARGIN_PADDING_PIXELS));
        canvas.drawText(quadrant1Name, marginFromRight, SizeConstants.MARGIN_PADDING_PIXELS, paint);

        quadrantTitleDetails.add(getDetailsForLeftAlignedQuadrantTitle(quadrant2Name, paint, SizeConstants.MARGIN_PADDING_PIXELS, SizeConstants.MARGIN_PADDING_PIXELS));
        canvas.drawText(quadrant2Name, SizeConstants.MARGIN_PADDING_PIXELS, SizeConstants.MARGIN_PADDING_PIXELS, paint);

        quadrantTitleDetails.add(getDetailsForLeftAlignedQuadrantTitle(quadrant3Name, paint, SizeConstants.MARGIN_PADDING_PIXELS, marginFromBottom));
        canvas.drawText(quadrant3Name, SizeConstants.MARGIN_PADDING_PIXELS, marginFromBottom, paint);

        quadrantTitleDetails.add(getDetailsForRightAlignedQuadrantTitle(quadrant4Name, paint, marginFromRight, marginFromBottom));
        canvas.drawText(quadrant4Name, marginFromRight, marginFromBottom, paint);
    }

    private QuadrantTitleDetail getDetailsForLeftAlignedQuadrantTitle(String quadrantName, Paint paint, float x, float y){
        paint.setTextAlign(Paint.Align.LEFT);
        float density = displayMetrics.density;
        float textWidthInPixels = paint.measureText(quadrantName);
        int textSize = (int) (SizeConstants.QUADRANT_TEXT_SIZE * density);
        return new QuadrantTitleDetail(x,y-textSize,x+textWidthInPixels,y);
    }

    private QuadrantTitleDetail getDetailsForRightAlignedQuadrantTitle(String quadrantName, Paint paint, float x, float y){
        paint.setTextAlign(Paint.Align.RIGHT);
        float density = displayMetrics.density;
        float textWidthInPixels = paint.measureText(quadrantName);
        int textSize = (int) (SizeConstants.QUADRANT_TEXT_SIZE * density);
        return new QuadrantTitleDetail(x-textWidthInPixels,y-textSize,x,y);
    }


    @Override
    protected void determineMaxRadiusAndOrigins() {
        screenOriginY = displayMetrics.heightPixels / 2;
        screenOriginX = -displayMetrics.widthPixels / 2;
        maxRadius = (displayMetrics.widthPixels / 2) - SizeConstants.RADIUS_MARGIN_PIXELS;
    }

    @Override
    protected int getQuadrantStartTheta() {
        return 0;
    }

    @Override
    protected int getQuadrantEndTheta() {
        return 360;
    }

    @Override
    protected int getThetaAdjustmentForOverlap(Blip blip) {
        throw new IllegalStateException("No overlap possible for Quadrant 0");
    }



}
