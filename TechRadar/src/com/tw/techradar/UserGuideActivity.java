package com.tw.techradar;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ViewFlipper;
import com.tw.techradar.support.HorizontalSwipeDetector;

/**
 * Created with IntelliJ IDEA.
 * User: srideep
 * Date: 25/10/12
 * Time: 4:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserGuideActivity extends Activity implements View.OnClickListener, View.OnTouchListener {

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
        flipper.setOnClickListener(this);
        flipper.setOnTouchListener(this);

    }


    @Override
    public void onClick(View view) {
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
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