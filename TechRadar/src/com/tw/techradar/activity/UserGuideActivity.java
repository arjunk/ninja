package com.tw.techradar.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ViewFlipper;
import com.tw.techradar.R;
import com.tw.techradar.support.HorizontalSwipeDetector;

public class UserGuideActivity extends Activity {

    private ViewFlipper flipper;
    private GestureDetector leftSwipeDetector;
    private GestureDetector rightSwipeDetector;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);

        leftSwipeDetector = new GestureDetector(new HorizontalSwipeDetector(200,100,200, HorizontalSwipeDetector.SwipeType.RIGHT_TO_LEFT));
        rightSwipeDetector = new GestureDetector(new HorizontalSwipeDetector(200,100,200, HorizontalSwipeDetector.SwipeType.LEFT_TO_RIGHT));

        flipper = (ViewFlipper) findViewById(R.id.flipper);
        flipper.setDisplayedChild(0);

    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (leftSwipeDetector.onTouchEvent(motionEvent)){
            flipper.setInAnimation(this, R.anim.slide_in_left);
            flipper.setOutAnimation(this, R.anim.slide_out_left);

            if (flipper.getDisplayedChild() + 1< flipper.getChildCount())
                flipper.showNext();
            return true;
        }else if (rightSwipeDetector.onTouchEvent(motionEvent)) {
            flipper.setInAnimation(this, R.anim.slide_in_right);
            flipper.setOutAnimation(this, R.anim.slide_out_right);

            if (flipper.getDisplayedChild()!=0)
                flipper.showPrevious();
            return true;
        }
        return false;
    }
}