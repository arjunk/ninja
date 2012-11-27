package com.tw.techradar.support.gestures;

import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import com.tw.techradar.constants.SizeConstants;

public class RadarGestureDetector implements View.OnTouchListener{
    public static final int DOUBLE_TAP_TIMEOUT = 500;
    private ScaleGestureDetector scaleGestureDetector;
    private final RadarGestureDetector.ScaleListener scaleListener;
    private RadarGestureListener radarGestureListener;
    private RadarMessagePump radarMessagePump;
    private long lastTouchTime;

    private enum RadarGestureMessage{
        ZOOM_IN,
        ZOOM_OUT,
        CLICK,
        DOUBLE_CLICK;

        public int getCode(){
            int code = 0;
            for (RadarGestureMessage gestureMessage : RadarGestureMessage.values()) {
                if (this == gestureMessage){
                    return code;
                }
                code++;
            }
            return -1;
        }
    }

    private class RadarMessagePump extends Handler{
        private static final int MESSAGE_DELAY_MS = 250;

        public void postRadarMessage(RadarGestureMessage gestureMessage, Point point){
            removeAllMessages();
            sendMessageDelayed(Message.obtain(this, gestureMessage.getCode(), point), MESSAGE_DELAY_MS);
        }

        private void removeAllMessages() {
            this.removeCallbacksAndMessages(null);
        }

        @Override
        public void handleMessage(Message msg) {
            RadarGestureMessage radarMessage = RadarGestureMessage.values()[msg.what];
            switch (radarMessage){
            case ZOOM_IN:
                radarGestureListener.onPinchZoomIn((Point)msg.obj);
                break;

            case ZOOM_OUT:
                radarGestureListener.onPinchZoomOut((Point)msg.obj);
                break;

            case CLICK:
                radarGestureListener.onClick((Point)msg.obj);
                break;

            case DOUBLE_CLICK:
                radarGestureListener.onDoubleClick((Point) msg.obj);
                break;
            }
        }

    }

    public RadarGestureDetector(View mainView, RadarGestureListener radarGestureListener) {
        this.radarMessagePump = new RadarMessagePump();
        mainView.setOnTouchListener(this);
        this.radarGestureListener = radarGestureListener;
        this.scaleListener = new ScaleListener();
        this.scaleGestureDetector = new ScaleGestureDetector(mainView.getContext(), scaleListener);

    }

    private boolean isDoubleTap(){
        long thisTime = System.currentTimeMillis();
        if((thisTime - lastTouchTime) < DOUBLE_TAP_TIMEOUT) {
            lastTouchTime = -1;
            return true;
        }
        lastTouchTime = thisTime;
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            if (!isDoubleTap()){
                radarMessagePump.postRadarMessage(RadarGestureMessage.CLICK, new Point((int) event.getX(), (int) event.getY()));
            }
            else{
                radarMessagePump.postRadarMessage(RadarGestureMessage.DOUBLE_CLICK, new Point((int) event.getX(), (int) event.getY()));
            }
        }
        scaleGestureDetector.onTouchEvent(event);

        return true;
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (detector.getScaleFactor() >= SizeConstants.PINCH_ZOOM_IN_DETECTION_THRESHOLD){
                radarMessagePump.postRadarMessage(RadarGestureMessage.ZOOM_IN, new Point((int) detector.getFocusX(), (int) detector.getFocusY()));
                return false;

            }else if (detector.getScaleFactor() <= SizeConstants.PINCH_ZOOM_OUT_DETECTION_THRESHOLD) {
                radarMessagePump.postRadarMessage(RadarGestureMessage.ZOOM_OUT, new Point((int) detector.getFocusX(), (int) detector.getFocusY()));
                return false;
            }
            return true;

        }
    }
}
