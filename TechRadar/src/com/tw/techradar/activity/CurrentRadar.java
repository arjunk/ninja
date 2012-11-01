package com.tw.techradar.activity;

import android.graphics.Path;
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

import java.util.ArrayList;
import java.util.List;

public class CurrentRadar extends Activity {

	private int marginX;
    private int marginY;

    List<Blip> blips = new ArrayList<Blip>(108);

    class Blip {
        private float xCoordinate;
        private float yCoordinate;
        private BlipType blipType;

        public float getxCoordinate() {
            return xCoordinate;
        }

        public float getyCoordinate() {
            return yCoordinate;
        }

        public BlipType getBlipType() {
            return blipType;
        }

        Blip(float xCoordinate, float yCoordinate, BlipType blipType) {
            this.xCoordinate = xCoordinate;
            this.yCoordinate = yCoordinate;
            this.blipType = blipType;
        }
    }

    enum BlipType{
        Triangle(6),Circle(5);
        private int radius;

        BlipType(int radius) {

            this.radius = radius;
        }

        public int getRadius() {
            return radius;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_radar);

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
        
        drawRadarCircles(centerX, centerY, multiplier, canvas,paint);
        drawTriangleBlip(canvas, 210, 60, multiplier);
        drawCircleBlip(canvas, 375, 235, multiplier);
        picture.endRecording();
        

        PictureDrawable drawable = new PictureDrawable(picture);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.currentRadarLayout);
        layout.setBackgroundDrawable(drawable);
        
    }

    private void drawTriangleBlip(Canvas canvas, int radius, int theta, float radiusMultiplier) {
        float xCoordinate = getXCoordinate(radius*radiusMultiplier,theta);
        float yCoordinate = getYCoordinate(radius*radiusMultiplier, theta);


		 System.out.println(String.format("Plotting Triangle at %f %f", xCoordinate, yCoordinate));

		 Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		    paint.setStrokeWidth(2);
		    paint.setColor(android.graphics.Color.BLUE);
		    paint.setStyle(Paint.Style.FILL_AND_STROKE);
		    paint.setAntiAlias(true);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);

        float topPointX = xCoordinate;
        int radiusOfCircumscribedCircle = 6;
        float topPointY = yCoordinate - radiusOfCircumscribedCircle;

        float bottomRightX = xCoordinate + (radiusOfCircumscribedCircle*3)/(2*FloatMath.sqrt(3));
        float bottomRightY = yCoordinate + radiusOfCircumscribedCircle/2;

        float bottomLeftX = xCoordinate - (radiusOfCircumscribedCircle*3)/(2*FloatMath.sqrt(3));
        float bottomLeftY = bottomRightY;

        path.moveTo(topPointX, topPointY);
        path.lineTo(bottomRightX, bottomRightY);
        path.lineTo(bottomLeftX, bottomLeftY);
        path.lineTo(topPointX, topPointY);
        path.close();

        canvas.drawPath(path, paint);

        blips.add(new Blip(xCoordinate,yCoordinate,BlipType.Triangle));

    }


    private void determineBoundsForView(View mainView) {
		int bounds[] = new int[2];
		mainView.getLocationOnScreen(bounds);
		this.marginX = bounds[0];
		this.marginY = bounds[1];
		System.out.println(String.format("MarginX %d MarginY %d", this.marginX, this.marginY));
		
	}


	private void drawCircleBlip(Canvas canvas, int radius, int theta, float radiusMultiplier) {
        float xCoordinate = getXCoordinate(radius*radiusMultiplier,theta);
        float yCoordinate = getYCoordinate(radius*radiusMultiplier, theta);
        
		 
		 System.out.println(String.format("Plotting at %f %f", xCoordinate, yCoordinate));
		 
		 Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		    paint.setStrokeWidth(2);
		    paint.setColor(android.graphics.Color.BLUE);     
		    paint.setStyle(Paint.Style.FILL_AND_STROKE);
		    paint.setAntiAlias(true);
		 
		 canvas.drawCircle(xCoordinate, yCoordinate, 5, paint);

         blips.add(new Blip(xCoordinate,yCoordinate,BlipType.Circle));
		
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
            double D = Math.pow(blip.getxCoordinate() - clickX, 2) + Math.pow(blip.getyCoordinate() - (clickY - marginY), 2);
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
