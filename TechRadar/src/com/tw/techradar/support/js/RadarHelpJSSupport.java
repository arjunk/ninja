package com.tw.techradar.support.js;

import android.content.SharedPreferences;
import android.view.View;
import android.webkit.WebView;

public class RadarHelpJSSupport {
    public static final String HELP_KEY = "need_help";
    private WebView webView;
    private View radarContainer;
    private SharedPreferences user_data;

    public RadarHelpJSSupport(WebView webView, View radarContainer, SharedPreferences user_data) {
        this.webView = webView;
        this.radarContainer = radarContainer;
        this.user_data = user_data;
    }

    public void init() {
        webView.addJavascriptInterface(this, this.getClass().getSimpleName());
    }

    public void cleanup(){
        webView.removeJavascriptInterface(this.getClass().getSimpleName());
    }

    public boolean getDoNotShowAgainFlag(){
        return user_data.getBoolean(HELP_KEY,false);
    }

    public void closeHelpWindow(boolean doNotShowAgainFlag){
        SharedPreferences.Editor editor = user_data.edit();
        editor.putBoolean(HELP_KEY, doNotShowAgainFlag);
        editor.commit();
        radarContainer.bringToFront();
    }
}
