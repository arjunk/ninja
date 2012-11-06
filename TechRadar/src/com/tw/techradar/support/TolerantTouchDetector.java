package com.tw.techradar.support;

import com.tw.techradar.constants.SizeConstants;
import com.tw.techradar.ui.model.Blip;

import java.util.List;

public class TolerantTouchDetector {

    private int fingerTipRadius;
    private List<Blip> radarBlips;

    public TolerantTouchDetector(float dpi, List<Blip> radarBlips){
        this.radarBlips = radarBlips;
        this.fingerTipRadius = Math.round(SizeConstants.FINGER_TIP_DETECTION_RADIUS * dpi);
    }

    public Blip getClosestBlipForTouchEvent(float x, float y){
        Blip blipTouched = null;
        double minDistance = Double.MAX_VALUE;


        for (Blip radarBlip : radarBlips) {
            if (radarBlip.isPointInBlip(x,y)){
                //If exact touch then return immediately
                blipTouched = radarBlip;
                break;
            }

            double distanceMeasure = getDistanceMeasure(radarBlip, x, y);
            float toleranceRadius = radarBlip.getRadius() + fingerTipRadius;

            if (distanceMeasure <= toleranceRadius){
                //Blip lies within touch radius
                if (distanceMeasure < minDistance){
                    //Keep track of closest blip
                    minDistance = distanceMeasure;
                    blipTouched = radarBlip;
                }
            }

        }

        return blipTouched;

    }

    private double getDistanceMeasure(Blip blip, float x, float y){
        return Math.sqrt(Math.pow(blip.getXCoordinate() - x, 2) +  Math.pow(blip.getYCoordinate() - y,2));
    }
}
