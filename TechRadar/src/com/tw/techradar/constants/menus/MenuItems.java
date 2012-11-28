package com.tw.techradar.constants.menus;

import android.support.v4.app.Fragment;
import com.tw.techradar.R;
import com.tw.techradar.views.fragments.RadarFragment;
import com.tw.techradar.views.fragments.WebViewFragment;

public enum MenuItems {
    INTRO("Intro",WebViewFragment.class,"file:///android_asset/html/introduction.html", R.drawable.intro_radar),
    ABOUT("About",WebViewFragment.class,"file:///android_asset/html/about.html", R.drawable.about_radar),
    RADAR("Radar",RadarFragment.class,null, R.drawable.radar_icon),
    REFERENCES("References",WebViewFragment.class,"file:///android_asset/html/radar_references.html", R.drawable.references_radar);
    private String title;
    private Class<? extends Fragment> fragmentClass;
    private String url;
    private int toolbarImageId;

    MenuItems(String title, Class<? extends Fragment> fragmentClass, String url, int toolbarImageId) {
        this.title = title;
        this.fragmentClass = fragmentClass;
        this.url = url;
        this.toolbarImageId = toolbarImageId;
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

    public int getToolbarImageId() {
        return toolbarImageId;
    }
}
