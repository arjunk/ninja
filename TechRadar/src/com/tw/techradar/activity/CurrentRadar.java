package com.tw.techradar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableLayout;
import com.tw.techradar.R;
import com.tw.techradar.controller.RadarController;
import com.tw.techradar.model.Radar;
import com.tw.techradar.model.RadarItem;
import com.tw.techradar.support.RadarView;
import com.tw.techradar.ui.model.Blip;

import java.util.List;

public class CurrentRadar extends Activity {

    private List<Blip> blips = null;
    private DisplayMetrics displayMetrics;
    private Radar radarData;
    private View mainView;
    private TableLayout mainLayout;
    private RadarView radarView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_radar);
        radarData = getRadarData();
        mainView = findViewById(R.id.currentRadarLayout);
        mainLayout = (TableLayout) findViewById(R.id.currentRadarLayout);
        radarView = new RadarView(0, radarData,mainView, mainLayout,this);
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



    @Override
	public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            Blip blip = radarView.getBlipClicked(event.getX(), event.getY());
            if (blip != null) {
                System.out.println("Click lies on a " + blip.getClass() + " Blip");
                displayItemInfo(blip);
            } else {
                System.out.println("Click does not lie on a Blip");
                switchRadarView(event.getX(), event.getY());

            }
        }
    	return super.onTouchEvent(event);
	}

    private void switchRadarView(float x, float y) {
        if (radarView.getCurrentQuadrant() != 0)
            radarView.switchQuadrant(0);
        else {
            radarView.switchQuadrant(radarView.getQuadrantClicked(x,y));
        }
    }

    private void displayItemInfo(Blip blip) {
        Intent intent = new Intent(this, ItemInfoActivity.class);
        intent.putExtra(RadarItem.ITEM_KEY, blip.getRadarItem());
        startActivity(intent);
    }


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_current_radar, menu);
        return true;
    }

}
