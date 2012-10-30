package com.tw.techradar;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.drawable.PictureDrawable;
import android.graphics.Picture;
import android.graphics.Point;
import android.util.FloatMath;
import android.view.Display;
import android.view.Menu;
import android.widget.RelativeLayout;

public class CurrentRadar extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_radar);
        
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        
        int centerX = screenWidth/2;
        int centerY = screenHeight/2;

        int maxRadius = 290;
        float multiplier = (float)maxRadius/400;
        
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
        plotTriangle(canvas, 210, 60, multiplier);
        drawRadarCircles(centerX, centerY, multiplier, canvas,paint);      
        picture.endRecording();
        

        PictureDrawable drawable = new PictureDrawable(picture);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.currentRadarLayout);
        layout.setBackgroundDrawable(drawable);
        
    }

	private void plotTriangle(Canvas canvas,int radius,int theta, float radiusMultiplier) {
		 float xCoordinate = getXCoordinate(radius*radiusMultiplier,theta);
		 float yCoordinate = getYCoordinate(radius*radiusMultiplier, theta);
		 
		 System.out.println(String.format("Plotting at %f %f",xCoordinate,yCoordinate));
		 
		 Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		    paint.setStrokeWidth(2);
		    paint.setColor(android.graphics.Color.BLUE);     
		    paint.setStyle(Paint.Style.FILL_AND_STROKE);
		    paint.setAntiAlias(true);
		    
		 canvas.drawCircle(xCoordinate, yCoordinate, 5, paint);
		
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
        canvas.drawCircle((float) centerX, (float) centerY, (float) (multiplier*150), circlePaint);
        canvas.drawCircle((float) centerX, (float) centerY, (float) (multiplier*275), circlePaint);
        canvas.drawCircle((float) centerX, (float) centerY, (float) (multiplier*350), circlePaint);
        canvas.drawCircle((float) centerX, (float) centerY, (float) (multiplier*400), circlePaint);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_current_radar, menu);
        return true;
    }
}
