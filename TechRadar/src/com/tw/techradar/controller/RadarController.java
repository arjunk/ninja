package com.tw.techradar.controller;

import android.content.res.AssetManager;
import com.tw.techradar.model.Radar;
import com.tw.techradar.model.RadarArc;
import com.tw.techradar.model.RadarItem;
import com.tw.techradar.model.RadarQuadrant;
import com.tw.techradar.util.JSONUtility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RadarController {

    private AssetManager assetManager;
    private String fileName = "json/radar.json";

    public RadarController(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Radar getRadarData() throws Exception {
        JSONObject jsonObject = JSONUtility.getJSONData(assetManager, fileName);
        return getRadarData(jsonObject);
    }

    private Radar getRadarData(JSONObject reader) throws JSONException {
        Radar radar = new Radar();

        radar.setItems(getRadarItems(reader));
        radar.setQuadrants(getRadarQuadrants(reader));
        radar.setRadarArcs(getRadarArcs(reader));
        radar.setName(getRadarTitle(reader));
        return radar;
    }

    private List<RadarQuadrant> getRadarQuadrants(JSONObject reader) throws JSONException {
        JSONArray radar_quadrants = reader.getJSONArray("radar_quadrants");
        List<RadarQuadrant> radarQuadrants = new ArrayList<RadarQuadrant>();

        for (int i=0; i< radar_quadrants.length(); i++){
            JSONObject jsonObject = radar_quadrants.getJSONObject(i);
            RadarQuadrant quadrant = new RadarQuadrant();

            quadrant.setName(jsonObject.getString("name"));
            quadrant.setTip(jsonObject.getString("tip"));
            quadrant.setStart(jsonObject.getInt("start"));
            quadrant.setEnd(jsonObject.getInt("end"));

            radarQuadrants.add(quadrant);
        }
        return radarQuadrants;
    }

    private List<RadarArc> getRadarArcs(JSONObject reader) throws JSONException {
        JSONArray radar_arcs = reader.getJSONArray("radar_arcs");
        List<RadarArc> radarArcs = new ArrayList<RadarArc>();
        RadarArc lastRadarArc = null;
        for (int i=0; i< radar_arcs.length(); i++){
            JSONObject jsonObject = radar_arcs.getJSONObject(i);
            int arcStartOffset = (lastRadarArc == null) ? 0 : lastRadarArc.getRadius();
            RadarArc radarArc = new RadarArc(jsonObject.getInt("r"),jsonObject.getString("name"),arcStartOffset);
            radarArcs.add(radarArc);
            lastRadarArc = radarArc;
        }

        return radarArcs;
    }

    private String getRadarTitle(JSONObject reader) throws JSONException {
        return reader.getString("radar_title");
    }

    private List<RadarItem> getRadarItems(JSONObject reader) throws JSONException {
        JSONArray radar_data = reader.getJSONArray("radar_data");
        List<RadarItem> radarItems = new ArrayList<RadarItem>();

        for (int i=0; i< radar_data.length(); i++){
            JSONObject jsonObject = radar_data.getJSONObject(i);
            RadarItem radarItem = new RadarItem();

            radarItem.setDescription(jsonObject.getString("description"));
            radarItem.setMovement(jsonObject.getString("movement"));
            radarItem.setName(jsonObject.getString("name"));
            radarItem.setTip(jsonObject.getString("tip"));
            radarItem.setTheta(jsonObject.getJSONObject("pc").getInt("t"));
            radarItem.setRadius(jsonObject.getJSONObject("pc").getInt("r"));

            radarItems.add(radarItem);
        }
        return  radarItems;
    }

}
