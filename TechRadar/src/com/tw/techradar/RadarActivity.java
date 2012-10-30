package com.tw.techradar;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;
import com.tw.techradar.controller.RadarController;
import com.tw.techradar.model.Radar;

public class RadarActivity extends Activity {
    private WebView webView;
    public void onCreate(Bundle savedInstanceState) {

         super.onCreate(savedInstanceState);
        AssetManager assetManager = getAssets();
        RadarController radarController = new RadarController(assetManager);

        Radar radarData = null;

        try {
            radarData = radarController.getRadarData();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        setContentView(R.layout.activity_radar);
        TextView view = (TextView) findViewById(R.id.radarData);
        view.setText(radarData.toString());
    }
}