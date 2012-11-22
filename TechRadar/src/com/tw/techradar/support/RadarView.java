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

    private Radar radarData;
    private View mainView;
    private DisplayMetrics displayMetrics;
    private Activity parentContext;
    private int marginX;
    private int marginY;
    private QuadrantView quadrantView;
    private Map<QuadrantView.QuadrantType,QuadrantView> quadrantViews;


    public RadarView(int currentQuadrant, Radar radarData, View mainView, Activity parentContext) {
        this.radarData = radarData;
        this.mainView = mainView;
        this.parentContext = parentContext;
    }

    public void drawRadar() {
        determineBoundsAndDimensions();

        if (!isQuadrantViewInitialized())
            initializeQuadrants(displayMetrics, mainView, radarData, marginX, marginY);

        quadrantView = getQuadrantViewFor(QuadrantView.QuadrantType.QUADRANT_0);
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

    public void switchQuadrant(QuadrantView.QuadrantType quadrantType) {
        quadrantView = getQuadrantViewFor(quadrantType);
        quadrantView.render();
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


    public QuadrantView.QuadrantType getQuadrantClicked(float x, float y) {
        return getQuadrantForPoint((int) x, (int) y).getQuadrantType();
    }

    public Blip getBlipClicked(float clickX, float clickY) {
        float correctedX = clickX - marginX;
        float correctedY = clickY - marginY;

        return quadrantView.getClosestBlipForTouchEvent(correctedX, correctedY);
    }

    public QuadrantView.QuadrantType getCurrentQuadrantType() {
        return quadrantView.getQuadrantType();
    }

    private void initializeQuadrants(DisplayMetrics displayMetrics, View mainView,Radar radarData,int marginX, int marginY){
        quadrantViews = new HashMap<QuadrantView.QuadrantType, QuadrantView>();
        //Important: First initialize all individual quadrants. Each individual quadrant will mutate specific sections of RadarData RadarItem objects as per the overlaps / collisions detected.
        //May need cleanup

        quadrantViews.put(QuadrantView.QuadrantType.QUADRANT_1, new Quadrant1View(displayMetrics,mainView,radarData,marginX,marginY));
        quadrantViews.put(QuadrantView.QuadrantType.QUADRANT_2, new Quadrant2View(displayMetrics,mainView,radarData,marginX,marginY));
        quadrantViews.put(QuadrantView.QuadrantType.QUADRANT_3, new Quadrant3View(displayMetrics,mainView,radarData,marginX,marginY));
        quadrantViews.put(QuadrantView.QuadrantType.QUADRANT_4, new Quadrant4View(displayMetrics,mainView,radarData,marginX,marginY));

        //Use the mutated RadarData RadarItems (corrected for collisions / overlaps) for Quadrant 0

        quadrantViews.put(QuadrantView.QuadrantType.QUADRANT_0, new AllQuadrantView(displayMetrics,mainView,radarData,marginX,marginY));
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

    private QuadrantView getQuadrantViewFor(QuadrantView.QuadrantType quadrantType) {
        return quadrantViews.get(quadrantType);
    }


}
