package com.tw.techradar.support.quadrants;

import android.graphics.*;
import android.graphics.drawable.PictureDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.Shape;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.View;
import com.tw.techradar.constants.SizeConstants;
import com.tw.techradar.model.Radar;
import com.tw.techradar.model.RadarArc;
import com.tw.techradar.model.RadarItem;
import com.tw.techradar.ui.model.Blip;

import java.util.*;

public abstract class QuadrantView {

    protected DisplayMetrics displayMetrics;
    private View mainView;
    protected Radar radarData;
    protected final int marginX;
    protected final int marginY;
    protected int screenOriginX;
    protected int screenOriginY;
    protected int maxRadius;
    protected float scalingFactor;
    protected List<Blip> blips;
    protected RadarArc radarArcFilter;
    protected String radarTextFilter;
    protected int marginFromRight;
    protected int marginFromBottom;
    private static Map<Integer,QuadrantView> quadrantViews;

    public static QuadrantView getQuadrantViewFor(int quadrantNo){
        return quadrantViews.get(quadrantNo);
    }

    public static void initializeQuadrants(DisplayMetrics displayMetrics, View mainView,Radar radarData,int marginX, int marginY){
        if (isQuadrantViewInitialized()) return;
        quadrantViews = new HashMap<Integer, QuadrantView>();
        quadrantViews.put(0, new AllQuadrantView(displayMetrics,mainView,radarData,marginX,marginY));
        quadrantViews.put(1, new Quadrant1View(displayMetrics,mainView,radarData,marginX,marginY));
        quadrantViews.put(2, new Quadrant2View(displayMetrics,mainView,radarData,marginX,marginY));
        quadrantViews.put(3, new Quadrant3View(displayMetrics,mainView,radarData,marginX,marginY));
        quadrantViews.put(4, new Quadrant4View(displayMetrics,mainView,radarData,marginX,marginY));
    }

    private static boolean isQuadrantViewInitialized() {
        return quadrantViews != null;
    }

    public QuadrantView(DisplayMetrics displayMetrics, View mainView,Radar radarData,int marginX, int marginY){

        this.displayMetrics = displayMetrics;
        this.mainView = mainView;
        this.radarData = radarData;
        this.marginX = marginX;
        this.marginY = marginY;
        determineMaxRadiusAndOrigins();
        determineScalingFactor();
        determineQuadrantCaptionMargins();
    }

    public static QuadrantView getQuadrantForPoint(int x, int y){
        for (QuadrantView quadrantView : quadrantViews.values()) {
            if (quadrantView.isPointInQuadrant(x,y)){
                return quadrantView;
            }
        }
        return null;
    }

    public List<Blip> getBlips(){
        return this.blips;
    }

    public QuadrantView filterWith(String textFilter){
        this.radarTextFilter = textFilter;
        return this;
    }

    public QuadrantView filterWith(RadarArc arcFilter){
        this.radarArcFilter = arcFilter;
        return this;
    }

    public QuadrantView clearFilter(){
        this.radarArcFilter = null;
        this.radarTextFilter = null;
        return this;
    }

    public abstract int getQuadrantNo();

    public void render(){
        initBlipsForRadarData();
        Picture picture = new Picture();
        Canvas canvas = picture.beginRecording(displayMetrics.widthPixels, displayMetrics.heightPixels);
        // Draw on the canvas

        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        int centerX = displayMetrics.widthPixels - Math.abs(screenOriginX);
        int centerY = displayMetrics.heightPixels - Math.abs(screenOriginY);

        drawBackground(canvas);
        drawRadarQuadrants(screenWidth, screenHeight, centerX, centerY, canvas,
                paint);
        drawRadarCircles(Math.abs(screenOriginX), Math.abs(screenOriginY),canvas, paint);
        drawRadarBlips(canvas);

        picture.endRecording();
        PictureDrawable drawable = new PictureDrawable(picture);
        mainView.setBackgroundDrawable(drawable);

    }

    protected Paint getQuadrantTextPaint(Paint.Align textAlign, int textSize) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextSize(textSize);
        p.setColor(Color.rgb(37, 170, 225));
        p.setTextAlign(textAlign);
        return p;
    }

    public boolean isPointInQuadrant(int x, int y) {
        int correctedX = x - marginX;
        int correctedY = y - marginY;
        return (correctedX >= getStartX()) && (correctedX <= getEndX()) && (correctedY >= getStartY()) && (correctedY <= getEndY());
    }

    protected abstract int getEndY();

    protected abstract int getStartY();

    protected abstract int getEndX();

    protected abstract int getStartX();


    protected abstract String getQuadrantName();

    protected abstract void renderQuadrantCaption(Canvas canvas);

    protected abstract void determineMaxRadiusAndOrigins();

    private void determineScalingFactor(){
        scalingFactor = (float) maxRadius / getRadiusOfOutermostArc(radarData.getRadarArcs());
    }

    private void initBlipsForRadarData() {
        List<RadarItem> radarItems = Collections.<RadarItem>unmodifiableList((radarArcFilter == null)? radarData.getItems() : radarData.getItemsForArc(radarArcFilter));
        if(radarTextFilter!= null && radarTextFilter.trim().length() > 0) {
            radarItems = radarData.getItemWithText(radarItems, radarTextFilter.trim());
        }
        this.blips = new ArrayList<Blip>(radarItems.size());
        for (RadarItem radarItem : radarItems) {
            Blip blip = getBlipItem(radarItem);
            blips.add(blip);
        }

    }

    private Blip getBlipItem(RadarItem radarItem) {
        float xCoordinate = getXCoordinate(radarItem);
        float yCoordinate = getYCoordinate(radarItem);
        return Blip.getBlipForRadarItem(radarItem, xCoordinate, yCoordinate, displayMetrics.xdpi);
    }

    private float getXCoordinate(RadarItem radarItem) {

        float xCoord = radarItem.getRadius() * scalingFactor * FloatMath.cos((float) Math.toRadians(radarItem.getTheta()));
        return translateXCoordinate(xCoord);
    }

    private float translateXCoordinate(float xCoord) {
        float transaltedXCoord = xCoord - screenOriginX;
        return transaltedXCoord;
    }


    private float getYCoordinate(RadarItem radarItem) {
        float yCoord = radarItem.getRadius() * scalingFactor * FloatMath.sin((float) Math.toRadians(radarItem.getTheta()));
        return translateYCoordinate(yCoord);
    }

    private float translateYCoordinate(float yCoord) {
        float transaltedYCoord = yCoord - screenOriginY;
        return -transaltedYCoord;
    }

    private void drawBackground(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.rgb(213,213,213));
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRect(0,0, mainView.getRight(),mainView.getBottom(),paint);
    }

    private void drawRadarQuadrants(int screenWidth, int screenHeight,
                                    int centerX, int centerY, Canvas canvas, Paint paint) {
        canvas.drawLine((float) 0, (float) centerY, (float) screenWidth, (float) centerY, paint);
        canvas.drawLine((float) centerX, (float) 0, (float) centerX, (float) screenHeight, paint);
        renderQuadrantCaption(canvas);
    }

    private void drawRadarCircles(int centerX, int centerY, Canvas canvas, Paint circlePaint) {
        for (RadarArc radarArc : radarData.getRadarArcs()) {
            float circleRadius = scalingFactor * radarArc.getRadius();
            drawRadarCircleWithTitle(centerX, centerY, circleRadius, canvas, circlePaint, radarArc);
        }
    }

    private void drawRadarCircleWithTitle(float centerX, float centerY, float circleRadius, Canvas canvas, Paint circlePaint, RadarArc radarArc) {
        Path circle = new Path();
        circle.addCircle(centerX, centerY, circleRadius, Path.Direction.CCW);
        drawCircle(canvas, circle, circlePaint);
        drawCircleTitle(canvas, circle, circleRadius, radarArc.getName(), circlePaint);
    }

    private void drawCircle(Canvas canvas, Path circle, Paint circlePaint) {
        Shape shape = new PathShape(circle, 1, 1);
        shape.resize(1, 1);
        shape.draw(canvas, circlePaint);
    }

    private void drawCircleTitle(Canvas canvas, Path circle, float circleRadius, String name, Paint circlePaint) {
        float hOffset =  circleRadius*2.25f;
        final float vOffset = getTextBounds(circlePaint, name).height() / 2;

        setPaintForCircleTitles(circlePaint);

        canvas.drawTextOnPath(name, circle, hOffset, vOffset, circlePaint);

        restorePaintSettingsForDrawingOtherthanTitles(circlePaint);
    }

    private Rect getTextBounds(Paint paint, String text) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds;
    }

    private void setPaintForCircleTitles(Paint circlePaint) {
        circlePaint.setTextSize(20);
        circlePaint.setColor(Color.GRAY);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setTextAlign(Paint.Align.CENTER);
    }

    private void restorePaintSettingsForDrawingOtherthanTitles(Paint circlePaint) {
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(Color.WHITE);
    }

    private void drawRadarBlips(Canvas canvas) {
        for (Blip blip : blips) {
            blip.render(canvas, getQuadrantNo());
        }
    }

    private float getRadiusOfOutermostArc(List<RadarArc> radarArcs) {
        float maxRadius = 0.0f;
        for (RadarArc arc : radarArcs) {
            if (arc.getRadius() > maxRadius) {
                maxRadius = arc.getRadius();
            }
        }
        return maxRadius;
    }

    private void determineQuadrantCaptionMargins(){
        marginFromRight = displayMetrics.widthPixels - SizeConstants.MARGIN_PADDING_PIXELS;
        marginFromBottom = displayMetrics.heightPixels - SizeConstants.MARGIN_PADDING_PIXELS;
    }

}
