package com.tw.techradar.ui.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.tw.techradar.constants.SizeConstants;
import com.tw.techradar.model.RadarItem;

public class CircleBlip extends Blip {

    public CircleBlip(float xCoordinate, float yCoordinate, RadarItem radarItem, float displayDensityDPI) {
        super(xCoordinate, yCoordinate, radarItem);
        this.radius = Math.round(displayDensityDPI * SizeConstants.CIRCLE_BLIP_RADIUS_INCH);
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
