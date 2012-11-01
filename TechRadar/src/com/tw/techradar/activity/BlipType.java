package com.tw.techradar.activity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.FloatMath;

enum BlipType implements DrawableBlip{
    Triangle(6){
        public void drawOn(Canvas canvas, Blip blip) {

            System.out.println(String.format("Plotting Triangle at %f %f", blip.getXCoordinate(), blip.getYCoordinate()));

             Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setStrokeWidth(2);
                paint.setColor(android.graphics.Color.BLUE);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                paint.setAntiAlias(true);

            Path path = new Path();
            path.setFillType(Path.FillType.EVEN_ODD);

            float topPointX = blip.getXCoordinate();
            int radiusOfCircumscribedCircle = 6;
            float topPointY = blip.getYCoordinate() - radiusOfCircumscribedCircle;

            float bottomRightX = blip.getXCoordinate() + (radiusOfCircumscribedCircle*3)/(2* FloatMath.sqrt(3));
            float bottomRightY = blip.getYCoordinate() + radiusOfCircumscribedCircle/2;

            float bottomLeftX = blip.getXCoordinate() - (radiusOfCircumscribedCircle*3)/(2*FloatMath.sqrt(3));
            float bottomLeftY = bottomRightY;

            path.moveTo(topPointX, topPointY);
            path.lineTo(bottomRightX, bottomRightY);
            path.lineTo(bottomLeftX, bottomLeftY);
            path.lineTo(topPointX, topPointY);
            path.close();

            canvas.drawPath(path, paint);
        }

    },
    Circle(5){

        public void drawOn(Canvas canvas, Blip blip) {
            System.out.println(String.format("Plotting at %f %f", blip.getXCoordinate(), blip.getXCoordinate()));

             Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setStrokeWidth(2);
                paint.setColor(android.graphics.Color.BLUE);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                paint.setAntiAlias(true);

             canvas.drawCircle(blip.getXCoordinate(), blip.getYCoordinate(), 5, paint);
        }
    };
    private int radius;

    BlipType(int radius) {

        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }
}
