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

    public static String parseChanges(String json) {
        JSONObject obj = new JSONObject(json);
        String title = (String) obj.get("title");
        String timeStamp = (String) obj.getJSONObject("meta").get("dt");
        String user = (String) obj.get("user");
        StringBuilder builder = new StringBuilder();
        builder.append("title: " + title + " | " +
                "timestamp: " + timeStamp + " | " + "user: " +
                user + "                                               ");
        return builder.toString();
    }

    public static String getStats(String json) {
        JSONObject obj = new JSONObject(json);
        JSONArray array = obj.getJSONObject("query").getJSONArray("usercontribs");
        StringBuilder builder = new StringBuilder();
        builder.append("\t\tX\t\tY\n");
        String[] topics = new String[array.length()];
        for (int i = 0; i < array.length(); i++) {
            HashMap<String, Object> map =
                    (HashMap<String, Object>) array.getJSONObject(i).toMap();
            String size = String.valueOf(map.get("size"));
            String title = (String) map.get("title");
            topics[i] = title.contains("(") ?
                    title.substring(0, title.indexOf("(") - 1) : title;
            String timeStamp = (String) map.get("timestamp");
            if (Integer.parseInt(size) > 100) {
                builder.append("size: " + size + "\t" +
                        "timestamp: " + timeStamp + "\t"
                        + "ContributionType: adding new info" + "\t\n");
            } else {
                builder.append("size: " + size + "\t" +
                        "timestamp: " + timeStamp + "\t"
                        + "ContributionType: typo" + "\t\n");
            }

        }
        HashMap<String, Integer> topicAppearences =
                new HashMap<>();
        for (int i = 0; i < topics.length; i++) {
            int count = 0;
            for (int j = 0; j < topics.length; j++) {
                if (topics[j].contains(topics[i])) {
                    count++;
                }
            }
            topicAppearences.put(topics[i], count);
        }
        builder.append("Теми, в які користувач зробив найбільший внесок - " +
                topicAppearences.entrySet()
                        .stream().max((entry1, entry2) ->
                        entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey());
        return builder.toString();
    }
}
