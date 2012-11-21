package com.tw.techradar.support;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import com.tw.techradar.model.Radar;
import com.tw.techradar.model.RadarArc;
import com.tw.techradar.support.quadrants.*;
import com.tw.techradar.ui.model.Blip;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RadarView {

    private int currentQuadrant = 0;
    private Radar radarData;
    private View mainView;
    private DisplayMetrics displayMetrics;
    private Activity parentContext;
    private int marginX;
    private int marginY;
    private QuadrantView quadrantView;
    private Map<Integer,QuadrantView> quadrantViews;


    public RadarView(int currentQuadrant, Radar radarData, View mainView, Activity parentContext) {
        this.currentQuadrant = currentQuadrant;
        this.radarData = radarData;
        this.mainView = mainView;
        this.parentContext = parentContext;
    }

    public void drawRadar() {
        determineBoundsAndDimensions();

        if (!isQuadrantViewInitialized())
            initializeQuadrants(displayMetrics, mainView, radarData, marginX, marginY);

        quadrantView = getQuadrantViewFor(currentQuadrant);
        quadrantView.render();
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
        quadrantView = getQuadrantViewFor(quadrant);
        quadrantView.render();
        this.currentQuadrant = quadrant;
    }

    public void filterByRadarArc(RadarArc radarArc) {
        for (QuadrantView view : quadrantViews.values()) {
            view.filterWith(radarArc);
        }
        quadrantView.filterWith(radarArc).render();
    }

    public void filterBySearchText(String stringFilter) {
        for (QuadrantView view : quadrantViews.values()) {
            view.filterWith(stringFilter);
        }
        quadrantView.filterWith(stringFilter).render();
    }


    public int getQuadrantClicked(float x, float y) {
        return getQuadrantForPoint((int) x, (int) y).getQuadrantNo();
    }

    public Blip getBlipClicked(float clickX, float clickY) {
        float correctedX = clickX - marginX;
        float correctedY = clickY - marginY;

        return quadrantView.getClosestBlipForTouchEvent(correctedX, correctedY);
    }

    public int getCurrentQuadrant() {
        return currentQuadrant;
    }

    private void initializeQuadrants(DisplayMetrics displayMetrics, View mainView,Radar radarData,int marginX, int marginY){
        quadrantViews = new HashMap<Integer, QuadrantView>();
        //Important: First initialize all individual quadrants. Each individual quadrant will mutate specific sections of RadarData RadarItem objects as per the overlaps / collisions detected.
        //May need cleanup

        quadrantViews.put(1, new Quadrant1View(displayMetrics,mainView,radarData,marginX,marginY));
        quadrantViews.put(2, new Quadrant2View(displayMetrics,mainView,radarData,marginX,marginY));
        quadrantViews.put(3, new Quadrant3View(displayMetrics,mainView,radarData,marginX,marginY));
        quadrantViews.put(4, new Quadrant4View(displayMetrics,mainView,radarData,marginX,marginY));

        //Use the mutated RadarData RadarItems (corrected for collisions / overlaps) for Quadrant 0

        quadrantViews.put(0, new AllQuadrantView(displayMetrics,mainView,radarData,marginX,marginY));
        initializeQuadrantData(quadrantViews.values());
    }

    private boolean isQuadrantViewInitialized() {
        return quadrantViews != null;
    }


    //Initialize each quadrant - also adjust for overlaps and collisions
    private static void initializeQuadrantData(Collection<QuadrantView> quadrantViews) {
        for (QuadrantView quadrantView : quadrantViews) {
            quadrantView.initialize();
        }
    }

    private QuadrantView getQuadrantForPoint(int x, int y){
        for (QuadrantView quadrantView : quadrantViews.values()) {
            if (quadrantView.isPointInQuadrant(x,y)){
                return quadrantView;
            }
        }
        return null;
    }

    private QuadrantView getQuadrantViewFor(int currentQuadrant) {
        return quadrantViews.get(currentQuadrant);
    }


}
