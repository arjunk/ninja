package com.tw.techradar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import com.tw.techradar.R;
import com.tw.techradar.controller.RadarController;
import com.tw.techradar.model.Radar;
import com.tw.techradar.model.RadarItem;
import com.tw.techradar.support.RadarView;
import com.tw.techradar.ui.model.Blip;

public class CurrentRadar extends Activity {

    private View mainView;
    private RadarView radarView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_radar);
        mainView = findViewById(R.id.currentRadarLayout);
        radarView = new RadarView(0,getRadarData(),mainView, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainView.post(new Runnable() {  //Required to ensure that drawRadar() is called only after view is rendered completely
            @Override
            public void run() {
                radarView.drawRadar();
            }
        });
    }

    private Radar getRadarData() {
        Radar radarData = null;
        try {
            return new RadarController(getAssets()).getRadarData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return radarData;
    }



    @Override
	public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            Blip blip = radarView.getBlipClicked(event.getX(), event.getY());
            if (blip != null) {
                System.out.println("Click lies on a " + blip.getClass() + " Blip");
                displayItemInfo(blip);
            } else {
                System.out.println("Click does not lie on a Blip");

                switchRadarView(event.getX(), event.getY());

            }
        }
    	return super.onTouchEvent(event);
	}

    private void switchRadarView(float x, float y) {
        if (radarView.getCurrentQuadrant() != 0)
            applyRotation(0, 180, 90);
        else {
//            radarView.switchQuadrant(radarView.getQuadrantClicked(x,y));
            applyRotation(radarView.getQuadrantClicked(x,y), 0, 180);

        }
    }

    private void displayItemInfo(Blip blip) {
        Intent intent = new Intent(this, ItemInfoActivity.class);
        intent.putExtra(RadarItem.ITEM_KEY, blip.getRadarItem());
        startActivity(intent);
    }


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_current_radar, menu);
        return true;
    }

    /**
     * Setup a new 3D rotation on the container view.
     *
     * @param position the item that was clicked to show a picture, or -1 to show the list
     * @param start the start angle at which the rotation must begin
     * @param end the end angle of the rotation
     */
    private void applyRotation(int position, float start, float end) {
        // Find the center of the container
        final float centerX = mainView.getWidth() / 2.0f;
        final float centerY = mainView.getHeight() / 2.0f;

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final Rotate3dAnimation rotation =
                new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
        rotation.setDuration(500);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView(position));

        mainView.startAnimation(rotation);
    }

    /**
     * This class listens for the end of the first half of the animation.
     * It then posts a new action that effectively swaps the views when the container
     * is rotated 90 degrees and thus invisible.
     */
    private final class DisplayNextView implements Animation.AnimationListener {
        private final int mPosition;

        private DisplayNextView(int position) {
            mPosition = position;
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            mainView.post(new SwapViews(mPosition));
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    /**
     * This class is responsible for swapping the views and start the second
     * half of the animation.
     */
    private final class SwapViews implements Runnable {
        private final int mPosition;

        public SwapViews(int position) {
            mPosition = position;
        }

        public void run() {
            final float centerX = mainView.getWidth() / 2.0f;
            final float centerY = mainView.getHeight() / 2.0f;
            Rotate3dAnimation rotation;

            if (mPosition > 0) {
                radarView.switchQuadrant(mPosition);
                rotation = new Rotate3dAnimation(180, 360, centerX, centerY, 310.0f, false);
            } else {
                radarView.switchQuadrant(0);
                rotation = new Rotate3dAnimation(90, 0, centerX, centerY, 310.0f, false);
            }

            rotation.setDuration(500);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());

            mainView.startAnimation(rotation);
        }
    }




}
