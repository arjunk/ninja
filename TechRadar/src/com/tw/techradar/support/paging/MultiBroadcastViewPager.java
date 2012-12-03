package com.tw.techradar.support.paging;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

public class MultiBroadcastViewPager extends ViewPager {
    private List<ViewPager.OnPageChangeListener> listeners = new ArrayList<OnPageChangeListener>();

    public MultiBroadcastViewPager(Context context) {
        super(context);
        super.setOnPageChangeListener(new PageScrollListener(listeners));
    }

    public MultiBroadcastViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addPageChangeListener(OnPageChangeListener listener){
        listeners.add(listener);

    }

    public void removePageChangeListener(OnPageChangeListener listener){
        listeners.remove(listener);
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        addPageChangeListener(listener);
    }

    private class PageScrollListener implements OnPageChangeListener {
        private  List<OnPageChangeListener> listeners;

        public PageScrollListener(List<OnPageChangeListener> listeners){
            this.listeners = listeners;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            for (OnPageChangeListener listener : listeners) {
                listener.onPageScrolled(position, positionOffset,positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            for (OnPageChangeListener listener : listeners) {
                listener.onPageSelected(position);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            for (OnPageChangeListener listener : listeners) {
                listener.onPageScrollStateChanged(state);
            }

        }
    }
}
