package com.tw.techradar.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import com.tw.techradar.R;
import com.tw.techradar.controller.RadarController;
import com.tw.techradar.model.Radar;
import com.tw.techradar.model.RadarArc;
import com.tw.techradar.model.RadarItem;
import com.tw.techradar.support.RadarView;
import com.tw.techradar.ui.model.Blip;

import java.util.List;

public class CurrentRadar extends Activity implements ActionBar.TabListener {

    private View mainView;
    private RadarView radarView;
    private long lastTouchTime = 0;
    private Radar radarData;
    private final static String ALL_ITEMS_TAB_TEXT = "All";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_radar);

        mainView = findViewById(R.id.currentRadarLayout);
        radarData = getRadarData();
        radarView = new RadarView(0, radarData,mainView, this);
        createTabs(radarData.getRadarArcs());

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            Blip blip = radarView.getBlipClicked(event.getX(), event.getY());
            if (blip != null) {
                System.out.println("Click lies on a " + blip.getClass() + " Blip");
                displayItemInfo(blip);
            } else if(isDoubleTap(event)){
                System.out.println("Click does not lie on a Blip");

                switchRadarView(event.getX(), event.getY());

            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        radarView.filterByRadarArc((RadarArc) tab.getTag());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    private void createTabs(List<RadarArc> radarArcs) {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        for (RadarArc radarArc : radarArcs) {
            createTab(radarArc.getName(),radarArc);
        }
        createTab(ALL_ITEMS_TAB_TEXT, null);
        actionBar.setSelectedNavigationItem(radarArcs.size());
    }


    @Override
    protected void onResume() {
        super.onResume();
        mainView.post(new Runnable() {  //Required to ensure that drawRadar() is called only after view is rendered completely
            @Override
            public void run() {
                radarView.drawRadar();
            }
        });
    }

    private Radar getRadarData() {
        Radar radarData = null;
        try {
            return new RadarController(getAssets()).getRadarData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return radarData;
    }

    private boolean isDoubleTap(MotionEvent event){
        long thisTime = event.getEventTime();
        if((thisTime - lastTouchTime) < 250) {
            lastTouchTime = -1;
            return true;
        }
        lastTouchTime = thisTime;
        return false;
    }

    private void switchRadarView(float x, float y) {
        if (radarView.getCurrentQuadrant() != 0){
            radarView.switchQuadrant(0);
        }
        else {
            radarView.switchQuadrant(radarView.getQuadrantClicked(x, y));

        }
    }

    private void displayItemInfo(Blip blip) {
        Intent intent = new Intent(this, ItemInfoActivity.class);
        intent.putExtra(RadarItem.ITEM_KEY, blip.getRadarItem());
        startActivity(intent);
    }

    private void createTab(String text, RadarArc radarArc) {
        ActionBar actionBar = getActionBar();
        ActionBar.Tab tab = actionBar.newTab();
        tab.setText(text);
        tab.setTag(radarArc);
        tab.setTabListener(this);
        actionBar.addTab(tab);
    }


}
