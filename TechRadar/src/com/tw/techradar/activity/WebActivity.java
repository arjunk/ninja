package com.tw.techradar.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import com.tw.techradar.R;

/**
 * Created with IntelliJ IDEA.
 * User: srideep
 * Date: 26/10/12
 * Time: 12:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class WebActivity extends Activity {
    private WebView webView;
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web);
        webView = (WebView) findViewById(R.id.web_view);

        loadWebContent();
    }

    private void loadWebContent() {
        webView.loadUrl("file:///android_asset/html/radar_references.html");
    }
}