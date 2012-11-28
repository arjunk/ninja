package com.tw.techradar.constants.menus;

import android.support.v4.app.Fragment;
import com.tw.techradar.views.fragments.RadarFragment;
import com.tw.techradar.views.fragments.WebViewFragment;

public enum MenuItems {
    INTRO("Intro",WebViewFragment.class,"file:///android_asset/html/introduction.html"),
    ABOUT("About",WebViewFragment.class,"file:///android_asset/html/about.html"),
    RADAR("Radar",RadarFragment.class,null),
    REFERENCES("References",WebViewFragment.class,"file:///android_asset/html/radar_references.html");
    private String title;
    private Class<? extends Fragment> fragmentClass;
    private String url;

    MenuItems(String title, Class<? extends Fragment> fragmentClass, String url) {
        this.title = title;
        this.fragmentClass = fragmentClass;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public Class<? extends Fragment> getFragmentClass() {
        return fragmentClass;
    }

    public String getUrl() {
        return url;
    }
}
