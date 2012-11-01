package com.tw.techradar.activity;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.PictureDrawable;
import android.graphics.Picture;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import com.tw.techradar.R;
import com.tw.techradar.controller.RadarController;
import com.tw.techradar.model.Radar;
import com.tw.techradar.model.RadarArc;
import com.tw.techradar.model.RadarItem;

import java.util.ArrayList;
import java.util.List;

public class CurrentRadar extends Activity {

	private int marginX;
    private int marginY;

    List<Blip> blips = new ArrayList<Blip>(108);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_radar);

        Radar radarData = getRadarData();

        View mainView = findViewById(R.id.currentRadarLayout);
        determineBoundsForView(mainView);
        
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        
        int centerX = screenWidth/2;
        int centerY = screenHeight/2;

        int maxRadius = (screenWidth /2) - 10;
        float multiplier = (float)maxRadius/getRadiusOfOutermostArc(radarData.getRadarArcs());
        
     // Add the radar to the RadarRL
        Picture picture = new Picture();
        Canvas canvas = picture.beginRecording(screenWidth, screenHeight);
        // Draw on the canvas

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xFF000000);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth((float) 0.8);

        drawRadarQuadrants(screenWidth, screenHeight, centerX, centerY, canvas,
                paint);
        drawRadarCircles(centerX, centerY, multiplier, canvas,paint);
        drawRadarBlips(multiplier, canvas, radarData);

        picture.endRecording();
        PictureDrawable drawable = new PictureDrawable(picture);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.currentRadarLayout);
        layout.setBackgroundDrawable(drawable);
        
    }

    private float getRadiusOfOutermostArc(List<RadarArc> radarArcs) {
        float maxRadius = 0.0f;
        for (RadarArc arc : radarArcs) {
             if (arc.getRadius()> maxRadius){
                 maxRadius = arc.getRadius();
             }
        }
        return maxRadius;
    }

    private Radar getRadarData() {
        Radar radarData = null;
        try {
            return new RadarController(getAssets()).getRadarData();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return radarData;
    }

    private void drawRadarBlips(float multiplier, Canvas canvas, Radar radarData) {
        for (RadarItem radarItem : radarData.getItems()) {
            float xCoordinate = getXCoordinate(radarItem.getRadius() * multiplier, radarItem.getTheta());
            float yCoordinate = getYCoordinate(radarItem.getRadius() * multiplier, radarItem.getTheta());
            Blip blip = new Blip(xCoordinate, yCoordinate, getBlipType(radarItem));
            blip.drawOn(canvas);
        }
    }

    private BlipType getBlipType(RadarItem radarItem) {
        BlipType blipType;
        if (radarItem.getMovement().equals("t")) {
            blipType = BlipType.Triangle;
        } else {
            blipType = BlipType.Circle;
        }
        return blipType;
    }


    private void determineBoundsForView(View mainView) {
		int bounds[] = new int[2];
		mainView.getLocationOnScreen(bounds);
		this.marginX = bounds[0];
		this.marginY = bounds[1];
		System.out.println(String.format("MarginX %d MarginY %d", this.marginX, this.marginY));
		
	}


    private float getXCoordinate(float radius, float theta) {
		
		float xCoord =  radius*FloatMath.cos((float)Math.toRadians(theta));
		System.out.println(FloatMath.cos(60));
		System.out.println(String.format("Converted radius %f and theta %f to %f",radius,theta,xCoord));
		return translateXCoordinate(xCoord);
	}
	
	private float translateXCoordinate(float xCoord){
		int screenOrigin = -getWindowManager().getDefaultDisplay().getWidth()/2;
		float transaltedXCoord = xCoord - screenOrigin;
		return transaltedXCoord;
	}


	private float getYCoordinate(float radius, float theta) {
		float yCoord = radius*FloatMath.sin((float)Math.toRadians(theta));
		return translateYCoordinate(yCoord);
	}
	
	private float translateYCoordinate(float yCoord){
		int screenOrigin = getWindowManager().getDefaultDisplay().getHeight()/2;
		float transaltedYCoord = yCoord - screenOrigin;
		return -transaltedYCoord;
	}


	private void drawRadarQuadrants(int screenWidth, int screenHeight,
			int centerX, int centerY, Canvas canvas, Paint paint) {
		canvas.drawLine((float)0, (float)centerY, (float)screenWidth, (float)centerY, paint);
        canvas.drawLine((float)centerX, (float)0, (float)centerX, (float)screenHeight, paint);
	}

	private void drawRadarCircles(int centerX, int centerY, float multiplier,
			Canvas canvas,Paint circlePaint) {
        canvas.drawCircle((float) centerX, (float) centerY, (multiplier*150), circlePaint);
        canvas.drawCircle((float) centerX, (float) centerY, (multiplier*275), circlePaint);
        canvas.drawCircle((float) centerX, (float) centerY, (multiplier*350), circlePaint);
        canvas.drawCircle((float) centerX, (float) centerY, (multiplier*400), circlePaint);
	}
	

    @Override
	public boolean onTouchEvent(MotionEvent event) {
    	System.out.println("MarginY :"+ marginY);
    	System.out.println("X:"+(event.getX() - marginX) + "  Y:" + (event.getY() - marginY));
    	Blip blip = doesLieInABlip(event.getX(),event.getY());
        if(blip!=null){
            System.out.println("Click lies on a "+blip.getBlipType()+" Blip");
        }
        else {
            System.out.println("Click does not lie on a Blip");
        }
    	return super.onTouchEvent(event);
	}
    
    public Blip doesLieInABlip(float clickX, float clickY){
        View mainView = findViewById(R.id.currentRadarLayout);
        determineBoundsForView(mainView);
        for (Blip blip : blips) {
            double D = Math.pow(blip.getXCoordinate() - clickX, 2) + Math.pow(blip.getYCoordinate() - (clickY - marginY), 2);
            if (D<= blip.getBlipType().getRadius()*blip.getBlipType().getRadius())
            {
                return blip;
            }
        }
        return null;
    }


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_current_radar, menu);
        return true;
    }
}
