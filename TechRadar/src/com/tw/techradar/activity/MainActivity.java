package com.tw.techradar.activity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import com.tw.techradar.R;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void goToUserGuide(View view){
        Intent intent = new Intent(this, UserGuideActivity.class);
        startActivity(intent);
    }

    public void goToReferences(View view){
        Intent intent = new Intent(this, WebActivity.class);
        startActivity(intent);
    }

    public void goToAboutRadar(View view){
    	Intent intent = new Intent(this, AboutRadarActivity.class);
    	startActivity(intent);
    }

    public void goToCurrentRadar(View view){
    	Intent intent = new Intent(this, CurrentRadar.class);
    	startActivity(intent);
    }
}