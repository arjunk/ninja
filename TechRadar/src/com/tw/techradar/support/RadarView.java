package com.tw.techradar.support;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.View;
import android.widget.TableLayout;
import com.tw.techradar.R;
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
    private TableLayout mainLayout;
    private DisplayMetrics displayMetrics;
    private Activity parentContext;
    private int marginX;
    private int marginY;
    private int screenOriginY;
    private int screenOriginX;
    private int maxRadius;
    private List<Blip> blips;


    public RadarView(int currentQuadrant, Radar radarData, View mainView, TableLayout mainLayout, Activity parentContext) {
        this.currentQuadrant = currentQuadrant;
        this.radarData = radarData;
        this.mainView = mainView;
        this.mainLayout = mainLayout;
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
        mainLayout.setBackgroundDrawable(drawable);
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

        for (Blip blip : blips) {
            if (blip.isPointInBlip(correctedX, correctedY))
                return blip;
        }
        return null;
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
            Blip blip = Blip.getBlipForRadarItem(radarItem, xCoordinate, yCoordinate);
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
