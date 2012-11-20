package com.tw.techradar.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.*;
import com.tw.techradar.R;
import com.tw.techradar.controller.RadarController;
import com.tw.techradar.model.Radar;
import com.tw.techradar.model.RadarItem;
import com.tw.techradar.support.RadarView;
import com.tw.techradar.ui.model.Blip;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class CurrentRadar extends Activity implements ActionBar.TabListener, TextWatcher, AdapterView.OnItemSelectedListener {

    private View mainView;
    private RadarView radarView;
    private long lastTouchTime = 0;
    private Radar radarData;
    private final static String INTRO_URL = "file:///android_asset/html/introduction.html";
    private ViewFlipper radarViewFlipper;
    private WebView webView;
    private Properties menuItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_radar);

        mainView = findViewById(R.id.currentRadarLayout);
        radarData = getRadarData();
        radarView = new RadarView(0, radarData,mainView, this);

        populateRadarFilter();
        getActionBar().setDisplayShowTitleEnabled(false);

        webView = (WebView) findViewById(R.id.htmlView);

        radarViewFlipper = (ViewFlipper)findViewById(R.id.radarViewFlipper);
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
        radarView.filterSearchText(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {
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
        mainView.getParent().requestChildFocus(mainView, findViewById(R.id.searchBox));
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

    private void initSearchListener() {
        EditText searchTextBox = (EditText) findViewById(R.id.searchBox);
        searchTextBox.addTextChangedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        String itemText = adapterView.getItemAtPosition(pos).toString();
        radarView.filterByRadarArc(radarData.getRadarArc(itemText));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
