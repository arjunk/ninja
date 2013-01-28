package com.tw.techradar.support.tabs;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import com.tw.techradar.R;
import com.tw.techradar.support.paging.MultiBroadcastViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public  class TabsAdapter extends FragmentPagerAdapter
        implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
    public static final int HIGHLIGHT_TINT_COLOR = -13388315;
    private final Context mContext;
    private final ActionBar mActionBar;
    private final MultiBroadcastViewPager mViewPager;
    private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
    private final Map<Integer,Fragment> fragments = new HashMap<Integer, Fragment>();
    private int tintColor ;

    static final class TabInfo {
        private final Class<?> clss;
        private final Bundle args;

        TabInfo(Class<?> _class, Bundle _args) {
            clss = _class;
            args = _args;
        }
    }

    public TabsAdapter(FragmentActivity activity, MultiBroadcastViewPager pager) {
        super(activity.getSupportFragmentManager());
        mContext = activity;
        mActionBar = activity.getActionBar();
        mViewPager = pager;
        tintColor = getTintColor();
        mViewPager.setAdapter(this);
        mViewPager.addPageChangeListener(this);
    }

    public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
        TabInfo info = new TabInfo(clss, args);
        tab.setTag(info);
        tab.setTabListener(this);
        mTabs.add(info);
        mActionBar.addTab(tab);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public Fragment getItem(int position) {
        TabInfo info = mTabs.get(position);
        Fragment fragment = Fragment.instantiate(mContext, info.clss.getName(), info.args);
        fragments.put(position, fragment);
        return fragment;
    }

    public Fragment getFragment(int position){
        return fragments.get(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mActionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        Object tag = tab.getTag();
        for (int i=0; i<mTabs.size(); i++) {
            if (mTabs.get(i) == tag) {
                tintTab(tab);
                mViewPager.setCurrentItem(i);
            }
            else{
                removeTintFromTab(mActionBar.getTabAt(i));
            }
        }
    }

    private int getTintColor() {
        return HIGHLIGHT_TINT_COLOR;
    }

    private void tintTab(ActionBar.Tab tab) {
        ImageView imgView = (ImageView) tab.getCustomView().findViewById(R.id.tabIcon);
        imgView.setColorFilter(tintColor);
    }
    private void removeTintFromTab(ActionBar.Tab tab) {
        ImageView imgView = (ImageView) tab.getCustomView().findViewById(R.id.tabIcon);
        imgView.setColorFilter(null);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }
}

