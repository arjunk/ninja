package com.tw.techradar.ui.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.tw.techradar.model.RadarItem;

public class CircleBlip extends Blip {



    public CircleBlip(float xCoordinate, float yCoordinate, RadarItem radarItem) {
        super(xCoordinate, yCoordinate, radarItem);
        this.radius = 5;
    }

    @Override
    public int getRadius() {
        return radius;
    }

    @Override
    public void render(Canvas canvas) {
        System.out.println(String.format("Plotting at %f %f", this.xCoordinate, this.yCoordinate));

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(2);
        paint.setColor(BLIP_COLOR);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);

        canvas.drawCircle(this.xCoordinate, this.yCoordinate, this.getRadius(), paint);
    }
}
