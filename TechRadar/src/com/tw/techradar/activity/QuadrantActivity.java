package com.tw.techradar.activity;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.RectF;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableLayout;
import com.tw.techradar.R;
import com.tw.techradar.controller.RadarController;
import com.tw.techradar.model.Radar;
import com.tw.techradar.model.RadarArc;
import com.tw.techradar.model.RadarItem;
import com.tw.techradar.ui.model.Blip;

import java.util.ArrayList;
import java.util.List;

public class QuadrantActivity extends Activity {

    private int marginX;
    private int marginY;
    private Paint paint;
    private int screenWidth;
    private int screenHeight;


    List<Blip> blips = new ArrayList<Blip>(108);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quadrant_view);

        Radar radarData = getRadarData();

        View mainView = findViewById(R.id.quadrantView);
        determineBoundsForView(mainView);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        int centerX = 0;
        int centerY = 0;

        int maxRadius = (screenWidth /2) - 10;
        float multiplier = (float)maxRadius/getRadiusOfOutermostArc(radarData.getRadarArcs());


        this.blips = getBlipsForRadarData(multiplier,radarData);
        Picture picture = new Picture();
        Canvas canvas = picture.beginRecording(screenWidth, screenHeight);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xFF000000);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) 0.8);

        drawRadarQuadrants(screenWidth, screenHeight, centerX, centerY, canvas,
                paint);
        drawArc(multiplier, R.id.quadrant1, 180, 90, canvas, centerX, centerY);
        drawRadarBlips(canvas);

        picture.endRecording();
        PictureDrawable drawable = new PictureDrawable(picture);
        TableLayout layout = (TableLayout) findViewById(R.id.quadrantView);
        layout.setBackgroundDrawable(drawable);

    }


    private void drawArc(float multiplier, int quadrant, int startAngle, int sweepAngel, Canvas canvas, int centerX, int centerY) {
        drawRadarArc(centerX, centerY, multiplier, canvas, startAngle, sweepAngel);
    }


    private void drawRadarArc(int centerX, int centerY, float multiplier, Canvas canvas, int startAngle, int sweepAngle) {

        final RectF oval = new RectF();

        drawArc(centerX, centerY, canvas, oval, multiplier*150, startAngle, sweepAngle);
        drawArc(centerX, centerY, canvas, oval, multiplier*275, startAngle, sweepAngle);
        drawArc(centerX, centerY, canvas, oval, multiplier*350, startAngle, sweepAngle);
        drawArc(centerX, centerY, canvas, oval, multiplier*400, startAngle, sweepAngle);
    }


    private void drawArc(int centerX, int centerY, Canvas canvas, RectF oval, float radius, int startAngle, int sweepAngle) {
        oval.set(centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius);
        canvas.drawArc(oval, startAngle, sweepAngle,true, paint);
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
            e.printStackTrace();
        }
        return radarData;
    }

    private List<com.tw.techradar.ui.model.Blip> getBlipsForRadarData(float multiplier,Radar radarData){
        List<com.tw.techradar.ui.model.Blip> blips = new ArrayList<com.tw.techradar.ui.model.Blip>(radarData.getItems().size());
        for (RadarItem radarItem : radarData.getItems()) {
            float xCoordinate = getXCoordinate(radarItem.getRadius() * multiplier, radarItem.getTheta());
            float yCoordinate = getYCoordinate(radarItem.getRadius() * multiplier, radarItem.getTheta());
            com.tw.techradar.ui.model.Blip blip = com.tw.techradar.ui.model.Blip.getBlipForRadarItem(radarItem, xCoordinate, yCoordinate);
            blips.add(blip);
        }

        return blips;
    }

    private void drawRadarBlips(Canvas canvas) {
        for (com.tw.techradar.ui.model.Blip blip : blips) {
            blip.render(canvas);
        }
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
                                  Canvas canvas,Paint circlePaint, List<RadarArc> radarArcs) {
        for (RadarArc radarArc : radarArcs) {
            canvas.drawCircle((float) centerX, (float) centerY, (multiplier*radarArc.getRadius()), circlePaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("MarginY :"+ marginY);
        System.out.println("X:"+(event.getX() - marginX) + "  Y:" + (event.getY() - marginY));
        com.tw.techradar.ui.model.Blip blip = doesLieInABlip(event.getX(),event.getY());
        if(blip!=null){
            System.out.println("Click lies on a "+ blip.getClass() +" Blip");
        }
        else {
            System.out.println("Click does not lie on a Blip");
        }
        return super.onTouchEvent(event);
    }

    public com.tw.techradar.ui.model.Blip doesLieInABlip(float clickX, float clickY){
        View mainView = findViewById(R.id.quadrantView);
        determineBoundsForView(mainView);
        for (com.tw.techradar.ui.model.Blip blip : blips) {
            if (blip.isPointInBlip(clickX,clickY - marginY))
                return blip;
        }
        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_current_radar, menu);
        return true;
    }

}
