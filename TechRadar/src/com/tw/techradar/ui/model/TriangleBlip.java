package com.tw.techradar.ui.model;

import android.graphics.*;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.FloatMath;
import com.tw.techradar.constants.SizeConstants;
import com.tw.techradar.model.RadarItem;

public class TriangleBlip extends Blip {

    public TriangleBlip(float xCoordinate, float yCoordinate, RadarItem radarItem, float displayDensityDPI) {
        super(xCoordinate, yCoordinate, radarItem);
        this.radius = Math.round(displayDensityDPI * SizeConstants.TRIANGLE_BLIP_RADIUS_INCH);
    }

    @Override
    public int getRadius() {
        return this.radius;
    }

    @Override
    public void render(Canvas canvas, int currentView) {
        System.out.println(String.format("Plotting Triangle at %f %f", this.xCoordinate, this.yCoordinate));

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(2);
        paint.setColor(BLIP_COLOR);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);

        float topPointX = this.xCoordinate;
        int radiusOfCircumscribedCircle = this.getRadius();
        float topPointY = this.yCoordinate - radiusOfCircumscribedCircle;

        float bottomRightX = this.xCoordinate + (radiusOfCircumscribedCircle*3)/(2* FloatMath.sqrt(3));
        float bottomRightY = this.yCoordinate + radiusOfCircumscribedCircle/2;

        float bottomLeftX = this.xCoordinate - (radiusOfCircumscribedCircle*3)/(2*FloatMath.sqrt(3));
        float bottomLeftY = bottomRightY;

        path.moveTo(topPointX, topPointY);
        path.lineTo(bottomRightX, bottomRightY);
        path.lineTo(bottomLeftX, bottomLeftY);
        path.lineTo(topPointX, topPointY);
        path.close();

        canvas.drawPath(path, paint);
        renderBlipTitlesIfQuadrantView(canvas, currentView, paint);
    }
}
