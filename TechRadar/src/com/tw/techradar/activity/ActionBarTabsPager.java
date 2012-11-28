package com.tw.techradar.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import com.tw.techradar.R;
import com.tw.techradar.constants.menus.MenuItems;
import com.tw.techradar.support.tabs.TabsAdapter;
import com.tw.techradar.views.fragments.RadarFragment;
import com.tw.techradar.views.fragments.WebViewFragment;

import java.io.InputStream;
import java.util.Properties;

public class ActionBarTabsPager extends FragmentActivity {
    private static final String CURRENT_TAB_KEY = "tab";
    ViewPager mViewPager;
    TabsAdapter mTabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.flipper);
        setContentView(mViewPager);

        final ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);



        mTabsAdapter = new TabsAdapter(this, mViewPager);
        for (MenuItems menuItem : MenuItems.values()) {
            mTabsAdapter.addTab(bar.newTab().setText(menuItem.getTitle()),
                    menuItem.getFragmentClass(), getArgsWithURL(menuItem.getUrl()));
        }
        if (savedInstanceState != null) {
            bar.setSelectedNavigationItem(savedInstanceState.getInt(CURRENT_TAB_KEY, 0));
        }
    }

    private Bundle getArgsWithURL(String someURL) {
        Bundle args = new Bundle();
        args.putString(WebViewFragment.URL_KEY, someURL);
        return args;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_TAB_KEY, getActionBar().getSelectedNavigationIndex());
        super.onSaveInstanceState(outState);
    }

}
