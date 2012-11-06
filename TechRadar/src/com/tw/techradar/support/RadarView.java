package com.tw.techradar.support;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
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
        this.blips = getBlipsForRadarData(multiplier, radarData);
        tolerantTouchDetector = new TolerantTouchDetector(displayMetrics.xdpi, this.blips);
        // Add the radar to the RadarRL
        Picture picture = new Picture();
        Canvas canvas = picture.beginRecording(displayMetrics.widthPixels, displayMetrics.heightPixels);
        // Draw on the canvas

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xFF000000);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) 0.8);
        int centerX = displayMetrics.widthPixels - Math.abs(screenOriginX);
        int centerY = displayMetrics.heightPixels - Math.abs(screenOriginY);

        drawRadarQuadrants(screenWidth, screenHeight, centerX, centerY, canvas,
                paint);
        drawRadarCircles(Math.abs(screenOriginX), Math.abs(screenOriginY), multiplier, canvas, paint, radarData.getRadarArcs());
        drawRadarBlips(canvas);

        picture.endRecording();
        PictureDrawable drawable = new PictureDrawable(picture);
        mainView.setBackgroundDrawable(drawable);
    }

    public void switchQuadrant(int quadrant) {
        this.currentQuadrant = quadrant;
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
        System.out.println(FloatMath.cos(60));
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

    private void drawRadarCircles(int centerX, int centerY, float multiplier,
                                  Canvas canvas, Paint circlePaint, List<RadarArc> radarArcs) {
        for (RadarArc radarArc : radarArcs) {
            canvas.drawCircle((float) centerX, (float) centerY, (multiplier * radarArc.getRadius()), circlePaint);
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

    private List<Blip> getBlipsForRadarData(float multiplier, Radar radarData) {
        List<Blip> blips = new ArrayList<Blip>(radarData.getItems().size());
        for (RadarItem radarItem : radarData.getItems()) {
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


}
