package com.example.wikianalyser.model;

import org.apache.commons.lang3.StringUtils;

public class XMLParser {
    public static String parse(String xml) {
        String[] items = xml.split("<item>");
        String item;
        String title;
        String pubDate;
        String author;
        StringBuilder builder = new StringBuilder();

        for (int i = 1; i < items.length; i++) {
            item = items[i];
            title = StringUtils.substringBetween(item, "<title>", "</title>");
            builder.append(title + " | ");
            pubDate = StringUtils.substringBetween(item, "<pubDate>", "</pubDate>");
            builder.append(pubDate + " | ");
            author = StringUtils.substringBetween(item, "<dc:creator>", "</dc:creator>");
            builder.append(author + "\n");
        }
        return builder.toString();
    }
}
