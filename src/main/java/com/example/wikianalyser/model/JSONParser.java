package com.example.wikianalyser.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class JSONParser {
    public static String parse(String json) {
        JSONObject obj = new JSONObject(json);
        JSONArray array = obj.getJSONObject("query").getJSONArray("usercontribs");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < array.length(); i++) {
            HashMap<String, Object> map =
                    (HashMap<String, Object>) array.getJSONObject(i).toMap();
            String size = String.valueOf(map.get("size"));
            String title = (String) map.get("title");
            String timeStamp = (String) map.get("timestamp");
            builder.append("size: " + size + " | " + "title: " +
                    title + " | " + "timestamp: " + timeStamp + "\n");
        }
        return builder.toString();
    }
}
