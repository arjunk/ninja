package com.tw.techradar.support.gestures;

import android.graphics.Point;
import android.view.MotionEvent;

public interface RadarGestureListener {
    public void onPinchZoomIn(Point point);
    public void onPinchZoomOut(Point point);
    public void onClick(Point point);
    public void onDoubleClick(Point point);
}
