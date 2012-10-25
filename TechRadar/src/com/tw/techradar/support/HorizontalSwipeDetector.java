package com.tw.techradar.support;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: srideep
 * Date: 25/10/12
 * Time: 8:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class HorizontalSwipeDetector extends GestureDetector.SimpleOnGestureListener {

    public enum SwipeType{
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT
    }

    private int minSwipeDistanceX;
    private int maxToleranceY;
    private int minVelocity;
    private SwipeType swipeType;

    public HorizontalSwipeDetector(int minSwipeDistanceX, int maxToleranceY, int minVelocity, SwipeType swipeType) {
        this.minSwipeDistanceX = minSwipeDistanceX;
        this.maxToleranceY = maxToleranceY;
        this.minVelocity = minVelocity;
        this.swipeType = swipeType;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (swipeType == SwipeType.RIGHT_TO_LEFT){
            return determineIfRightToLeftSwipe(e1,e2,velocityX);
        }else
            return determineIfLeftToRightSwipe(e1,e2,velocityX);
    }

    private boolean determineIfLeftToRightSwipe(MotionEvent e1, MotionEvent e2, float velocityX) {
        return  (((e2.getX() - e1.getX())  > minSwipeDistanceX) && (Math.abs(velocityX) >= minVelocity) && (Math.abs(e1.getY() - e2.getY()) <= maxToleranceY));
    }

    private boolean determineIfRightToLeftSwipe(MotionEvent e1, MotionEvent e2, float velocityX) {
        return  (((e1.getX() - e2.getX())  > minSwipeDistanceX) && (Math.abs(velocityX) >= minVelocity) && (Math.abs(e1.getY() - e2.getY()) <= maxToleranceY));
    }
}
