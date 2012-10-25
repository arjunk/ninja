package com.tw.techradar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class AboutRadarActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_radar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_about_radar, menu);
        return true;
    }
}
