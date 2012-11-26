package com.tw.techradar.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.webkit.WebView;
import android.widget.*;
import com.tw.techradar.R;
import com.tw.techradar.constants.SizeConstants;
import com.tw.techradar.controller.RadarController;
import com.tw.techradar.model.Radar;
import com.tw.techradar.model.RadarItem;
import com.tw.techradar.support.RadarView;
import com.tw.techradar.support.quadrants.QuadrantType;
import com.tw.techradar.ui.model.Blip;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class CurrentRadar extends Activity implements ActionBar.TabListener, TextWatcher, AdapterView.OnItemSelectedListener{

    private View mainView;
    private RadarView radarView;
    private long lastTouchTime = 0;
    private Radar radarData;
    private final static String INTRO_URL = "file:///android_asset/html/introduction.html";
    private ViewFlipper radarViewFlipper;
    private WebView webView;
    private Properties menuItems;
    private ScaleGestureDetector scaleGestureDetector;
    private DisplayMetrics displayMetrics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_radar);
        populateDisplayMetrics();

        mainView = findViewById(R.id.currentRadarLayout);
        radarData = getRadarData();
        radarView = new RadarView(displayMetrics,radarData,mainView);

        populateRadarFilter();
        getActionBar().setDisplayShowTitleEnabled(false);

        webView = (WebView) findViewById(R.id.htmlView);

        radarViewFlipper = (ViewFlipper)findViewById(R.id.radarViewFlipper);
        scaleGestureDetector = new ScaleGestureDetector(mainView.getContext(), new ScaleListener());
        initSearchListener();
        loadMenuItems();
        createTabsForMenuItems(menuItems);
        switchToWebView(INTRO_URL);
    }

    private void createTabsForMenuItems(Properties menuItems) {
        ActionBar actionBar  =getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        for (Map.Entry menuEntry : menuItems.entrySet()) {
            ActionBar.Tab tab = actionBar.newTab();
            tab.setText((String) menuEntry.getKey()).setTag(menuEntry.getValue());
            tab.setTabListener(this);
            actionBar.addTab(tab);
        }
    }

    private void loadMenuItems() {
        try {
            InputStream rawResource = getResources().openRawResource(R.raw.menu);
            menuItems = new Properties();
            menuItems.load(rawResource);
        } catch (Exception e) {
            System.err.println("ERROR: Cannot load menu items - cannot continue");
            finish();
        }
    }

    private void populateRadarFilter() {
        Spinner spinner = (Spinner) findViewById(R.id.radar_filter_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.radar_circles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //TODO: Problem here - Need to check. Priority not working correctly
        if (isSingleFingerTouchGesture(event))
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                Blip blip = radarView.getBlipClicked(event.getX(), event.getY());
                if (blip != null) {
                    System.out.println("Click lies on a " + blip.getClass() + " Blip");
                    displayItemInfo(blip);
                    return true;
                } else if(isDoubleTap(event)){
                    System.out.println("Click does not lie on a Blip");
                    switchRadarView(event.getX(), event.getY());
                    return true;
                }
            }
        }
        return scaleGestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    private boolean isSingleFingerTouchGesture(MotionEvent event) {
        return event.getPointerCount() == 1;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if (isRadarTabSelected(tab)){
            switchToRadarView();
        }else{
            switchToWebView((String) tab.getTag());
        }
    }

    private void switchToWebView(String url) {
        webView.loadUrl(url);
        radarViewFlipper.setDisplayedChild(1);
    }

    private void switchToRadarView() {
        radarViewFlipper.setDisplayedChild(0);
        radarView.drawRadar();
    }

    private boolean isRadarTabSelected(ActionBar.Tab tab) {
        return (tab.getTag() == null) || ((String)tab.getTag()).length() == 0;
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        radarView.filterBySearchText(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {
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
        if (radarView.getCurrentQuadrantType() != QuadrantType.QUADRANT_ALL){
            radarView.switchQuadrant(QuadrantType.QUADRANT_ALL);
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

    private void initSearchListener() {
        EditText searchTextBox = (EditText) findViewById(R.id.searchBox);
        searchTextBox.addTextChangedListener(this);
    }

    private void populateDisplayMetrics() {
        displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        String itemText = adapterView.getItemAtPosition(pos).toString();
        radarView.filterByRadarArc(radarData.getRadarArc(itemText));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isRadarView() && radarView.getCurrentQuadrantType() != QuadrantType.QUADRANT_ALL) {
                radarView.switchQuadrant(QuadrantType.QUADRANT_ALL);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean isRadarView() {
        return radarViewFlipper.getDisplayedChild() == 0;
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            System.out.println("SCALE FACTOR:" + detector.getScaleFactor());
            boolean isZoomed = radarView.isZoomed();
            if ((detector.getScaleFactor() >= SizeConstants.PINCH_ZOOM_IN_DETECTION_THRESHOLD) && (!isZoomed)){
                radarView.switchQuadrant(radarView.getQuadrantClicked(detector.getFocusX(),detector.getFocusY()));
                return true;

            }else if ((detector.getScaleFactor() <= SizeConstants.PINCH_ZOOM_OUT_DETECTION_THRESHOLD) && (isZoomed)){
                radarView.zoomOut();
                return true;
            }
            return false;
        }
    }
}
