package com.tw.techradar.util;

import android.content.res.AssetManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.techradar.model.Radar;
import com.tw.techradar.model.RadarArc;
import com.tw.techradar.model.RadarItem;
import com.tw.techradar.model.RadarQuadrant;
import com.tw.techradar.util.JsonUtility;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class RadarDataProvider {

    private AssetManager assetManager;
    private String fileName = "json/radar.json";
    private ObjectMapper objectMapper;

    public RadarDataProvider(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.objectMapper = new ObjectMapper();
    }

    public Radar getRadarData() throws Exception {
        return getRadarData(JsonUtility.getJSONData(assetManager, fileName));
    }

    private Radar getRadarData(JSONObject reader) throws JSONException, IOException {
        Radar radar = new Radar();

        radar.setItems(getRadarItems(reader));
        radar.setQuadrants(getRadarQuadrants(reader));
        radar.setRadarArcs(getRadarArcs(reader));
        radar.setName(getRadarTitle(reader));

        return radar;
    }

    private List<RadarItem> getRadarItems(JSONObject reader) throws JSONException, IOException {
        return objectMapper.readValue(reader.getJSONArray("radar_data").toString(), new TypeReference<List<RadarItem>>() {});
    }

    private List<RadarQuadrant> getRadarQuadrants(JSONObject reader) throws JSONException, IOException {
        return objectMapper.readValue(reader.getJSONArray("radar_quadrants").toString(), new TypeReference<List<RadarQuadrant>>() {});
    }

    private List<RadarArc> getRadarArcs(JSONObject reader) throws JSONException, IOException {
        List<RadarArc> radarArcs = objectMapper.readValue(reader.getJSONArray("radar_arcs").toString(), new TypeReference<List<RadarArc>>(){});

        determineArcOffsets(radarArcs);
        return radarArcs;
    }

    private void determineArcOffsets(List<RadarArc> radarArcs) {
        int lastRadius = 0;
        for (RadarArc radarArc : radarArcs) {
            radarArc.setStartOffset(lastRadius);
            lastRadius = radarArc.getRadius();
        }
    }

    private String getRadarTitle(JSONObject reader) throws JSONException {
        return reader.getString("radar_title");
    }

}
