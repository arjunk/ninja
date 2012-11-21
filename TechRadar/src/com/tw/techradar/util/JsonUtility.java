package com.tw.techradar.util;

import android.content.res.AssetManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JSONUtility {
    public static JSONObject getJSONData(AssetManager assetManager, String fileName) throws IOException, JSONException {

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(assetManager.open(fileName)));

        return new JSONObject(readFileAsString1(reader));
    }

    private static String readFileAsString1(BufferedReader reader) throws IOException, JSONException {
        StringBuffer fileData = new StringBuffer(1000);

        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }
}