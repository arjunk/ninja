package com.tw.techradar.support.js;

import android.content.SharedPreferences;
import android.view.View;
import android.webkit.WebView;

public class RadarHelpJSSupport {
    private WebView webView;
    private View radarContainer;

    public RadarHelpJSSupport(WebView webView,View radarContainer) {
        this.webView = webView;
        this.radarContainer = radarContainer;
        init();
    }

    public void init() {
        webView.addJavascriptInterface(this, this.getClass().getSimpleName());
    }

    public void cleanup(){
        webView.removeJavascriptInterface(this.getClass().getSimpleName());
    }

    public void closeHelpWindow(boolean doNotShowAgainFlag){
        radarContainer.bringToFront();
    }
}
