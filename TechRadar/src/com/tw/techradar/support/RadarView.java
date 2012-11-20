package com.tw.techradar.support;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import com.tw.techradar.model.Radar;
import com.tw.techradar.model.RadarArc;
import com.tw.techradar.support.quadrants.QuadrantView;
import com.tw.techradar.ui.model.Blip;

public class RadarView {

    private int currentQuadrant = 1;
    private Radar radarData;
    private View mainView;
    private DisplayMetrics displayMetrics;
    private Activity parentContext;
    private int marginX;
    private int marginY;
    private TolerantTouchDetector tolerantTouchDetector;
    private RadarArc radarArcFilter;
    private CharSequence searchText;
    private float multiplier;
    //    private boolean isFixed = false;
//    private static final int RETRY_LIMIT = 100;
    private QuadrantView quadrantView;

    public RadarView(int currentQuadrant, Radar radarData, View mainView, Activity parentContext) {
        this.currentQuadrant = currentQuadrant;
        this.radarData = radarData;
        this.mainView = mainView;
        this.parentContext = parentContext;
    }

    public void drawRadar() {
        determineBoundsAndDimensions();
        QuadrantView.initializeQuadrants(displayMetrics, mainView, radarData, marginX, marginY);
        quadrantView = QuadrantView.getQuadrantViewFor(currentQuadrant);
        quadrantView.render();
        tolerantTouchDetector = new TolerantTouchDetector(displayMetrics.xdpi, quadrantView.getBlips());
    }

    private void determineBoundsAndDimensions() {
        displayMetrics = new DisplayMetrics();
        parentContext.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        this.marginX = displayMetrics.widthPixels - mainView.getMeasuredWidth();
        this.marginY = displayMetrics.heightPixels - mainView.getMeasuredHeight();
        System.out.println(String.format("MarginX %d MarginY %d", this.marginX, this.marginY));

        displayMetrics.heightPixels = displayMetrics.heightPixels - marginY;
        displayMetrics.widthPixels = displayMetrics.widthPixels - marginX;
    }

    public void switchQuadrant(int quadrant) {
        quadrantView = QuadrantView.getQuadrantViewFor(quadrant);
        quadrantView.render();
        this.currentQuadrant = quadrant;
    }

    public void filterByRadarArc(RadarArc radarArc) {
        quadrantView.filterWith(radarArc).render();
    }

    public void filterBySearchText(String stringFilter) {
        quadrantView.filterWith(stringFilter).render();
    }


    public int getQuadrantClicked(float x, float y) {
        return QuadrantView.getQuadrantForPoint((int)x,(int)y).getQuadrantNo();
    }

    public Blip getBlipClicked(float clickX, float clickY) {
        float correctedX = clickX - marginX;
        float correctedY = clickY - marginY;

        return tolerantTouchDetector.getClosestBlipForTouchEvent(correctedX, correctedY);
    }

    public int getCurrentQuadrant() {
        return currentQuadrant;
    }


//
//    /**
//     * Adjusts for collisions - WIP code
//     * @param blips
//     */
//    private void adjustForCollisions(List<Blip> blips) {
//        if (currentQuadrant==0) return;
//        int retries = 0;
//        int collisionCount = 0;
//        boolean collisionFlag = false;
//        do{
//        retries++;
//        collisionFlag = false;
//        for (Blip referenceBlip : blips) {
//            if (!isRadarItemRenderable(referenceBlip.getRadarItem())) continue;
//            for (Blip blip : blips) {
//                if ((blip == referenceBlip) || (!isRadarItemRenderable(blip.getRadarItem()))) continue;
//                boolean isCollisionPresent = checkAndResolveCollision(referenceBlip, blip);
//                collisionFlag = collisionFlag | isCollisionPresent;
//                if (isCollisionPresent)
//                    collisionCount++;
//            }
//        }
//        }while(collisionFlag && (retries < RETRY_LIMIT));
//        System.out.println("Collisions detected for Quadrant[" + currentQuadrant + "]:" + collisionCount);
//    }
//
//    private boolean checkAndResolveCollision(Blip referenceBlip, Blip blip) {
//        boolean collisionFlag=false;
//        while (chkIfCollision(referenceBlip, blip)) {
//            System.out.println("Collision between " + referenceBlip.getRadarItem().getName() + " and " + blip.getRadarItem().getName());
//
//            int blipThetaAdj;
//            int refBlipThetaAdj;
//
//            blipThetaAdj = (referenceBlip.getRadarItem().getTheta() > blip.getRadarItem().getTheta()) ? -1 : 1;
//            refBlipThetaAdj = -blipThetaAdj;
//
//            if (!blip.isFrozen())
//                adjustTheta(blip, blipThetaAdj);
//
//            if (!referenceBlip.isFrozen())
//                adjustTheta(referenceBlip, refBlipThetaAdj);
//
//            if (!isBlipWithinQuadrantBoundariesForZoomedView(blip) && !blip.isFrozen()){
//                System.out.println("Collision - boundary condition reached for blip:" + blip.getRadarItem().getName() + ".Reverting theta and ignoring collision for now");
//                adjustTheta(blip, -blipThetaAdj);
//                blip.freeze();
//
//            }
//
//            if (!isBlipWithinQuadrantBoundariesForZoomedView(referenceBlip) && !referenceBlip.isFrozen()){
//                System.out.println("Collision - boundary condition reached for blip:"  + referenceBlip.getRadarItem().getName() + ".Reverting theta and ignoring collision for now");
//                adjustTheta(referenceBlip, - refBlipThetaAdj);
//                referenceBlip.freeze();
//            }
//
//            if (referenceBlip.isFrozen() && blip.isFrozen()){
//                System.out.println("Collision - unresolveable:" + referenceBlip.getRadarItem().getName() + " and " + blip.getRadarItem().getName());
//                //Collision cannot be solved
//                return false;
//            }
//
//            collisionFlag = true;
//        }
//        return collisionFlag;
//    }
//
//    /**
//     * Fixes quadrant overlaps for all quadrants
//     * Need to cleanup
//     */
//
//    public void fixQuadrantOverlapsAndCollisions(){
//        if (isFixed){
//            return;
//        }
//        for (int quadrantToFix = 1; quadrantToFix <= 4 ; quadrantToFix ++){
//            this.currentQuadrant = quadrantToFix;
//            determineOrigins(currentQuadrant);
//            initializeDataForCurrentQuadrant();
//            fixQuadrantOverlapsForCurrentQuadrant();
//            adjustForCollisions(this.blips);
//        }
//        this.currentQuadrant = 0;
//        isFixed = true;
//    }
//
//    /**
//     * Fixes quadrant overlaps for current quadrant
//     */
//    private void fixQuadrantOverlapsForCurrentQuadrant(){
//        if ((currentQuadrant == 0) || (isFixed)) return;
//        for (Blip blip : blips) {
//            if (blip.getRadarItem().getQuadrant() != currentQuadrant) continue;
//            int adj = fixQuadrantOverlapIfPresentForBlip(blip);
//            if (adj != 0){
//                System.out.println("Collision - Quadrant[" + blip.getRadarItem().getQuadrant() + "] Item - " + blip.getRadarItem().getName() + " ThetaAdj[" + adj + "] deg");
//                blip.freeze();
//            }
//        }
//    }
//
//    private int fixQuadrantOverlapIfPresentForBlip(Blip blip) {
//        int thetaAdj = 0;
//        int totalAdjustment = 0;
//        while (!isBlipWithinQuadrantBoundariesForZoomedView(blip)) {
//            if (((blip.getRadarItem().getQuadrant() == 1) && (blip.getDimensionsWithText().bottom > displayMetrics.heightPixels)) ||
//                    ((blip.getRadarItem().getQuadrant() == 2) && (blip.getDimensionsWithText().right > displayMetrics.widthPixels)) ||
//                    ((blip.getRadarItem().getQuadrant() == 3) && (blip.getDimensionsWithText().top < 0)) ||
//                    ((blip.getRadarItem().getQuadrant() == 4) && (blip.getDimensionsWithText().left < 0)))
//
//            {
//                thetaAdj = 1;
//            } else if (((blip.getRadarItem().getQuadrant() == 1) && (blip.getDimensionsWithText().left < 0)) ||
//                    ((blip.getRadarItem().getQuadrant() == 2) && (blip.getDimensionsWithText().bottom > displayMetrics.heightPixels)) ||
//                    ((blip.getRadarItem().getQuadrant() == 3) && (blip.getDimensionsWithText().right > displayMetrics.widthPixels)) ||
//                    ((blip.getRadarItem().getQuadrant() == 4) && (blip.getDimensionsWithText().top < 0))) {
//                thetaAdj = -1;
//            }
//            totalAdjustment = totalAdjustment + thetaAdj;
//            blip.getRadarItem().setTheta(blip.getRadarItem().getTheta() + thetaAdj);
//            adjustCoOrdsForTheta(blip);
//        }
//        return totalAdjustment;
//    }
//
//    private void adjustTheta(Blip blip, int adjTheta) {
//        RadarItem radarItem = blip.getRadarItem();
//        radarItem.setTheta(radarItem.getTheta() + adjTheta);
//        adjustCoOrdsForTheta(blip);
//    }
//
//    private void adjustCoOrdsForTheta(Blip blip) {
//        RadarItem radarItem = blip.getRadarItem();
//        float newXCoordinate = getXCoordinate(radarItem);
//        float newYCoordinate = getYCoordinate(radarItem);
//        blip.shiftCoOrdinates(newXCoordinate, newYCoordinate);
//    }
//
//    private boolean isBlipWithinQuadrantBoundariesForZoomedView(Blip blip) {
//        Rect dimensions = blip.getDimensionsWithText();
//        int maxX = displayMetrics.widthPixels;
//        int maxY = displayMetrics.heightPixels;
//
//        return (dimensions.right <= maxX) && (dimensions.bottom <= maxY) && (dimensions.left >= 0) && (dimensions.top >= 0);
//    }
//
//    private boolean chkIfCollision(Blip blipA, Blip blipB){
//        return blipA.getTextDimensions().intersect(blipB.getTextDimensions()) || blipA.getIconDimensions().intersect(blipB.getIconDimensions()) || blipA.getIconDimensions().intersect(blipB.getTextDimensions()) || blipB.getIconDimensions().intersect(blipA.getTextDimensions());
//    }


//
//    private boolean isRadarItemRenderable(RadarItem radarItem){
//        return (currentQuadrant == 0) || (currentQuadrant == radarItem.getQuadrant());
//    }


}
