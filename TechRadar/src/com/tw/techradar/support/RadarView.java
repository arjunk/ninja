package com.tw.techradar.support;

import android.app.Activity;
import android.graphics.*;
import android.graphics.drawable.PictureDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.Shape;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.View;
import com.tw.techradar.model.Radar;
import com.tw.techradar.model.RadarArc;
import com.tw.techradar.model.RadarItem;
import com.tw.techradar.ui.model.Blip;

import java.util.ArrayList;
import java.util.List;

public class RadarView {

    private int currentQuadrant;
    private Radar radarData;
    private View mainView;
    private DisplayMetrics displayMetrics;
    private Activity parentContext;
    private int marginX;
    private int marginY;
    private int screenOriginY;
    private int screenOriginX;
    private int maxRadius;
    private List<Blip> blips;
    private TolerantTouchDetector tolerantTouchDetector;
    private RadarArc radarArcFilter;
    private CharSequence searchText;

    public RadarView(int currentQuadrant, Radar radarData, View mainView,Activity parentContext) {
        this.currentQuadrant = currentQuadrant;
        this.radarData = radarData;
        this.mainView = mainView;
        this.parentContext = parentContext;
    }

    public void drawRadar() {
        determineBoundsForView(mainView);
        determineScreenDimensions();
        determineOrigins(currentQuadrant);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        float multiplier = (float) maxRadius / getRadiusOfOutermostArc(radarData.getRadarArcs());
        this.blips = getBlipsForRadarData(multiplier);
        tolerantTouchDetector = new TolerantTouchDetector(displayMetrics.xdpi, this.blips);
        // Add the radar to the RadarRL
        Picture picture = new Picture();
        Canvas canvas = picture.beginRecording(displayMetrics.widthPixels, displayMetrics.heightPixels);
        // Draw on the canvas

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        int centerX = displayMetrics.widthPixels - Math.abs(screenOriginX);
        int centerY = displayMetrics.heightPixels - Math.abs(screenOriginY);

        drawBackground(canvas);
        drawRadarQuadrants(screenWidth, screenHeight, centerX, centerY, canvas,
                paint);
        drawRadarCircles(Math.abs(screenOriginX), Math.abs(screenOriginY), multiplier, canvas, paint);
        drawRadarBlips(canvas);

        picture.endRecording();
        PictureDrawable drawable = new PictureDrawable(picture);
        mainView.setBackgroundDrawable(drawable);
    }

    public void switchQuadrant(int quadrant) {
        this.currentQuadrant = quadrant;
        drawRadar();
    }

    public void filterByRadarArc(RadarArc radarArc){
        this.radarArcFilter = radarArc;
        drawRadar();
    }

    public int getQuadrantClicked(float x, float y) {

        x = x - marginX;
        y = y - marginY;

        int midpointX = displayMetrics.widthPixels / 2;
        int midpointY = displayMetrics.heightPixels / 2;

        int quadrant = 0;

        if (x >= midpointX) {
            if (y <= midpointY) {
                quadrant = 1;
            } else
                quadrant = 4;
        } else {
            if (y <= midpointY) {
                quadrant = 2;
            } else
                quadrant = 3;

        }
        return quadrant;

    }

    public Blip getBlipClicked(float clickX, float clickY) {
        float correctedX = clickX - marginX;
        float correctedY = clickY - marginY;

        return tolerantTouchDetector.getClosestBlipForTouchEvent(correctedX,correctedY);
    }

    public int getCurrentQuadrant() {
        return currentQuadrant;
    }

    private void determineScreenDimensions() {
        displayMetrics = new DisplayMetrics();
        parentContext.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        displayMetrics.heightPixels = displayMetrics.heightPixels - marginY;
        displayMetrics.widthPixels = displayMetrics.widthPixels - marginX;
    }

    private void determineBoundsForView(View mainView) {
        int bounds[] = new int[2];
        mainView.getLocationOnScreen(bounds);
        this.marginX = bounds[0];
        this.marginY = bounds[1];
        System.out.println(String.format("MarginX %d MarginY %d", this.marginX, this.marginY));
    }

    private float getXCoordinate(float radius, float theta) {

        float xCoord = radius * FloatMath.cos((float) Math.toRadians(theta));
        System.out.println(String.format("Converted radius %f and theta %f to %f", radius, theta, xCoord));
        return translateXCoordinate(xCoord);
    }

    private float translateXCoordinate(float xCoord) {
        float transaltedXCoord = xCoord - screenOriginX;
        return transaltedXCoord;
    }


    private float getYCoordinate(float radius, float theta) {
        float yCoord = radius * FloatMath.sin((float) Math.toRadians(theta));
        return translateYCoordinate(yCoord);
    }

    private float translateYCoordinate(float yCoord) {
        float transaltedYCoord = yCoord - screenOriginY;
        return -transaltedYCoord;
    }

    //TODO: Spiked code.. Need to clean up and better encapsulate stuff
    private void determineOrigins(int quadrant) {
        switch (quadrant) {
            case 1:
                screenOriginY = displayMetrics.heightPixels;
                screenOriginX = 0;
                maxRadius = displayMetrics.widthPixels - 10;
                break;

            case 2:
                screenOriginY = displayMetrics.heightPixels;
                screenOriginX = -displayMetrics.widthPixels;
                maxRadius = displayMetrics.widthPixels - 10;
                break;

            case 3:
                screenOriginY = 0;
                screenOriginX = -displayMetrics.widthPixels;
                maxRadius = displayMetrics.widthPixels - 10;

                break;

            case 4:
                screenOriginY = 0;
                screenOriginX = 0;
                maxRadius = displayMetrics.widthPixels - 10;

                break;

            default:
                screenOriginY = displayMetrics.heightPixels / 2;
                screenOriginX = -displayMetrics.widthPixels / 2;
                maxRadius = (displayMetrics.widthPixels / 2) - 10;

        }
    }


    private void drawRadarQuadrants(int screenWidth, int screenHeight,
                                    int centerX, int centerY, Canvas canvas, Paint paint) {
        canvas.drawLine((float) 0, (float) centerY, (float) screenWidth, (float) centerY, paint);
        canvas.drawLine((float) centerX, (float) 0, (float) centerX, (float) screenHeight, paint);
        drawQuadrantNames(screenHeight, screenWidth, canvas);
    }

    private void drawQuadrantNames(int screenHeight, int screenWidth, Canvas canvas) {
        Paint.Align textAlign = Paint.Align.LEFT;
        // Quadrant Mappings 1=2 2=1 3=3 4=4
        int quadrantTextSize = 20;
        int zoomedQuadrantTextSize = 25;
        int marginFromRight = screenWidth - 30;
        int marginFromBottom = screenHeight - 30;
        int marginFromLeft = 30;
        int marginFromTop = 30;
        String quadrant1Name = radarData.getQuadrants().get(1).getName();
        String quadrant2Name = radarData.getQuadrants().get(0).getName();
        String quadrant3Name = radarData.getQuadrants().get(2).getName();
        String quadrant4Name = radarData.getQuadrants().get(3).getName();
        switch(currentQuadrant){
            case 0:
                canvas.drawText(quadrant2Name, marginFromLeft, marginFromTop, getQuadrantTextPaint(textAlign, quadrantTextSize));
                canvas.drawText(quadrant1Name, marginFromRight, marginFromTop, getQuadrantTextPaint(Paint.Align.RIGHT, quadrantTextSize));
                canvas.drawText(quadrant3Name, marginFromLeft, marginFromBottom, getQuadrantTextPaint(textAlign, quadrantTextSize));
                canvas.drawText(quadrant4Name, marginFromRight, marginFromBottom, getQuadrantTextPaint(Paint.Align.RIGHT, quadrantTextSize));
                break;
            case 1:
                canvas.drawText(quadrant1Name, marginFromRight, marginFromTop, getQuadrantTextPaint(Paint.Align.RIGHT, zoomedQuadrantTextSize));
                break;
            case 2:
                canvas.drawText(quadrant2Name, marginFromLeft, marginFromTop, getQuadrantTextPaint(Paint.Align.LEFT, zoomedQuadrantTextSize));
                break;
            case 3:
                canvas.drawText(quadrant3Name, marginFromLeft, marginFromBottom, getQuadrantTextPaint(Paint.Align.LEFT, zoomedQuadrantTextSize));
                break;
            case 4:
                canvas.drawText(quadrant4Name, marginFromRight, marginFromBottom, getQuadrantTextPaint(Paint.Align.RIGHT, zoomedQuadrantTextSize));
                break;
        }
    }

    private Paint getQuadrantTextPaint(Paint.Align textAlign, int textSize) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextSize(textSize);
        p.setColor(Color.rgb(37, 170, 225));
        p.setTextAlign(textAlign);
        return p;
    }

    private void drawRadarCircles(int centerX, int centerY, float multiplier, Canvas canvas, Paint circlePaint) {
        for (RadarArc radarArc : radarData.getRadarArcs()) {
            float circleRadius = multiplier * radarArc.getRadius();
            drawRadarCircleWithTitle(centerX, centerY, circleRadius, canvas, circlePaint, radarArc);
        }
    }


    private float getRadiusOfOutermostArc(List<RadarArc> radarArcs) {
        float maxRadius = 0.0f;
        for (RadarArc arc : radarArcs) {
            if (arc.getRadius() > maxRadius) {
                maxRadius = arc.getRadius();
            }
        }
        return maxRadius;
    }

    private List<Blip> getBlipsForRadarData(float multiplier) {
        List<RadarItem> radarItems = (radarArcFilter == null)? radarData.getItems() : radarData.getItemsForArc(radarArcFilter);
        if(searchText!= null && searchText.length() > 0) {
            radarItems = radarData.getItemWithText(radarItems, searchText);
        }
        List<Blip> blips = new ArrayList<Blip>(radarItems.size());
        for (RadarItem radarItem : radarItems) {
            float xCoordinate = getXCoordinate(radarItem.getRadius() * multiplier, radarItem.getTheta());
            float yCoordinate = getYCoordinate(radarItem.getRadius() * multiplier, radarItem.getTheta());
            Blip blip = Blip.getBlipForRadarItem(radarItem, xCoordinate, yCoordinate, displayMetrics.xdpi);
            blips.add(blip);
        }

        return blips;
    }

    private void drawRadarBlips(Canvas canvas) {
        for (Blip blip : blips) {
            blip.render(canvas);
        }
    }

    private void drawBackground(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.rgb(213,213,213));
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRect(mainView.getLeft(),mainView.getTop(), mainView.getRight(),mainView.getBottom(),paint);
    }


    public void filterSearchText(CharSequence charSequence) {
        this.searchText = charSequence;
        drawRadar();
    }

    private void drawRadarCircleWithTitle(float centerX, float centerY, float circleRadius, Canvas canvas, Paint circlePaint, RadarArc radarArc) {
        Path circle = new Path();
        Path circleTitles = new Path();
        circle.addCircle(centerX, centerY, circleRadius, Path.Direction.CCW);
        circleTitles.addCircle(centerX, centerY, circleRadius+5, Path.Direction.CCW);
        drawCircle(canvas, circle, circlePaint);
        drawCircleTitle(canvas, circleTitles, circleRadius, radarArc.getName(), circlePaint);
    }

    private void drawCircle(Canvas canvas, Path circle, Paint circlePaint) {
        Shape shape = new PathShape(circle, 1, 1);
        shape.resize(1, 1);
        shape.draw(canvas, circlePaint);
    }

    private void drawCircleTitle(Canvas canvas, Path circle, float circleRadius, String name, Paint circlePaint) {
        float hOffset =  circleRadius*2;
        final float vOffset = 0;

        setPaintForCircleTitles(circlePaint);

        canvas.drawTextOnPath(name, circle, hOffset, vOffset, circlePaint);

        restorePaintSettingsForDrawingOtherthanTitles(circlePaint);
    }

    private void restorePaintSettingsForDrawingOtherthanTitles(Paint circlePaint) {
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(Color.WHITE);
    }

    private void setPaintForCircleTitles(Paint circlePaint) {
        circlePaint.setTextSize(20);
        circlePaint.setColor(Color.GRAY);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setTextAlign(Paint.Align.CENTER);
    }

}
