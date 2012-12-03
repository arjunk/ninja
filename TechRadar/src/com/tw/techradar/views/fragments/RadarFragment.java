package com.tw.techradar.views.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.tw.techradar.R;
import com.tw.techradar.activity.ItemInfoActivity;
import com.tw.techradar.support.paging.FragmentMultibroadcastViewPageSupport;
import com.tw.techradar.support.paging.MultiBroadcastViewPager;
import com.tw.techradar.util.RadarDataProvider;
import com.tw.techradar.model.Radar;
import com.tw.techradar.model.RadarItem;
import com.tw.techradar.views.RadarView;
import com.tw.techradar.support.gestures.RadarGestureDetector;
import com.tw.techradar.support.gestures.RadarGestureListener;
import com.tw.techradar.views.model.Blip;

public class RadarFragment extends Fragment implements AdapterView.OnItemSelectedListener, RadarGestureListener, SearchView.OnQueryTextListener {
    private Radar radarData;
    private RadarView radarView;
    private View mainView;
    private RadarGestureDetector radarGestureDetector;
    private SearchView searchTextBox;
    private MultiBroadcastViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        radarData = getRadarData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.current_radar, container, false);
        View radarLayout = mainView.findViewById(R.id.currentRadarLayout);
        radarGestureDetector = new RadarGestureDetector(radarLayout, this,viewPager);
        radarView = new RadarView(radarData, radarLayout,getDisplayMetrics());
        drawRadarPostViewRendered();
        return mainView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FragmentMultibroadcastViewPageSupport){
            viewPager = ((FragmentMultibroadcastViewPageSupport) activity).getViewPager();
        }else{
            throw new IllegalStateException("Cannot initialize from a parent activity which does not implement FragmentMultibroadcastViewPageSupport");   //Should never come here
        }


    }

    private void drawRadarPostViewRendered() {
        mainView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isViewRendered()) {
                    mainView.getViewTreeObserver().removeGlobalOnLayoutListener(this); //Needed deprecated method for Honeycomb compatibility
                    radarView.drawRadar();
                    populateRadarFilter();
                    initSearchListener();
                }

            }

            private boolean isViewRendered() {
                return mainView.getMeasuredHeight() != 0 && mainView.getMeasuredWidth() != 0;
            }
        });
    }

    private DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    private Radar getRadarData() {
        Radar radarData = null;
        try {
            return new RadarDataProvider(getActivity().getAssets()).getRadarData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return radarData;
    }

    private void displayItemInfo(Blip blip) {
        Intent intent = new Intent(getActivity(), ItemInfoActivity.class);
        intent.putExtra(RadarItem.ITEM_KEY, blip.getRadarItem());
        startActivity(intent);
    }

    private void initSearchListener() {
        searchTextBox = (SearchView) mainView.findViewById(R.id.searchBox);
        searchTextBox.setOnQueryTextListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        String itemText = adapterView.getItemAtPosition(pos).toString();
        radarView.filterByRadarArc(radarData.getRadarArc(itemText));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void populateRadarFilter() {
        Spinner spinner = (Spinner) mainView.findViewById(R.id.radar_filter_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.radar_circles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onPinchZoomIn(Point point) {
        if (!radarView.isZoomed())
            radarView.switchQuadrant(radarView.getQuadrantClicked(point.x, point.y));
    }

    @Override
    public void onPinchZoomOut(Point point) {
        if (radarView.isZoomed())
            radarView.zoomOut();
    }

    @Override
    public void onClick(Point point) {
        Blip blip = radarView.getBlipClicked(point.x, point.y);
        if (blip != null) {
            System.out.println("Click lies on a " + blip.getClass() + " Blip");
            displayItemInfo(blip);
        }
        else if(isQuadrantTitleClicked(point.x, point.y)){
            radarView.switchQuadrant(radarView.getQuadrantClicked(point.x, point.y));
        }
    }

    private boolean isQuadrantTitleClicked(int x, int y) {
        return !radarView.isZoomed() && radarView.isQuadrantTitleClicked(x, y);
    }

    @Override
    public void onDoubleClick(Point point) {
        if (!radarView.isZoomed())
            radarView.switchQuadrant(radarView.getQuadrantClicked(point.x, point.y));
        else
            radarView.zoomOut();
    }

    public boolean isRadarZoomed() {
        return radarView.isZoomed();
    }

    public void zoomOut() {
        radarView.zoomOut();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onQueryTextChange(String query) {
        radarView.filterBySearchText(query);
        return true;
    }
}

