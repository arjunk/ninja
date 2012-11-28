package com.tw.techradar.views.model;

import android.graphics.Canvas;
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
    public void render(Canvas canvas, int currentView) {
        System.out.println(String.format("Plotting at %f %f", this.xCoordinate, this.yCoordinate));
        canvas.drawCircle(this.xCoordinate, this.yCoordinate, this.getRadius(), paint);

        renderBlipTitlesIfQuadrantView(canvas, currentView);

    }

    @Override
    public int getCorrectedRadiusOffsetYForText() {
        return radius;
    }

}
