package com.github.cris16228.webcore.models;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Document {

    private final int statusCode;
    private final Map<String, List<String>> headers;
    private final String body;
    private final URL url;
    private final String ID_REGEX_PART_1 = "<([a-zA-Z0-9]+)[^>]*\\bid=['\"]";
    private final String ID_REGEX_PART_2 = "['\"][^>]*>(.*?)</\\1>";
    private final String CLASS_REGEX_PART_1 = "<([a-zA-Z0-9]+)([^>]*\\sclass=['\"]([^'\"]*\\s)?";
    private final String CLASS_REGEX_PART_2 = "(\\s[^'\"]*)?['\"][^>]*)>(.*?)</\\1>";
    private final String ID_CLASS_REGEX_PART_1 = "<([a-zA-Z0-9]+)\\b[^>]*\\bid=['\"]";
    private final String ID_CLASS_REGEX_PART_2 = "['\"][^>]*\\bclass=['\"][^'\"]*";
    private final String ID_CLASS_REGEX_PART_3 = "[^'\"]*['\"][^>]*>(.*?)</\\1>";
    private final List<String> history;

    public Document(int statusCode, Map<String, List<String>> headers, String body, URL url, List<String> history) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
        this.url = url;
        this.history = history;
    }

    public URL getUrl() {
        return url;
    }

    public String getPath() {
        return url.getPath();
    }

    public String getHost() {
        return url.getHost();
    }

    public String getProtocol() {
        return url.getProtocol();
    }

    public String getPort() {
        return url.getPort() + "";
    }

    public String getQuery() {
        return url.getQuery();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public Element getElementById(String id) {
        Pattern pattern = Pattern.compile(ID_REGEX_PART_1 + id + ID_REGEX_PART_2, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            return new Element(matcher.group(0));
        }
        return null;
    }

    public List<Element> getElementsById(String id) {
        List<Element> elements = new ArrayList<>();
        Pattern pattern = Pattern.compile(ID_REGEX_PART_1 + id + ID_REGEX_PART_2, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(body);
        while (matcher.find()) {
            elements.add(new Element(matcher.group(0)));
        }
        return elements;
    }

    public Element getElementByClass(String className) {
        Pattern pattern = Pattern.compile(CLASS_REGEX_PART_1 + className + CLASS_REGEX_PART_2, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            return new Element(matcher.group(0));
        }
        return null;
    }

    public List<Element> getElementsByClass(String className) {
        List<Element> elements = new ArrayList<>();
        Pattern pattern = Pattern.compile(CLASS_REGEX_PART_1 + className + CLASS_REGEX_PART_2, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(body);
        while (matcher.find()) {
            elements.add(new Element(matcher.group(0)));
        }
        return elements;
    }

    public Element find(String id, String className) {
        Pattern pattern = Pattern.compile(ID_CLASS_REGEX_PART_1 + id + ID_CLASS_REGEX_PART_2 + className + ID_CLASS_REGEX_PART_3, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            return new Element(matcher.group(0));
        }
        return null;
    }

    public List<Element> findAll(String id, String className) {
        List<Element> elements = new ArrayList<>();
        Pattern pattern = Pattern.compile(ID_CLASS_REGEX_PART_1 + id + ID_CLASS_REGEX_PART_2 + className + ID_CLASS_REGEX_PART_3, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(body);
        while (matcher.find()) {
            elements.add(new Element(matcher.group(0)));
        }
        return elements;
    }

    public SocialCard getSocialCard() {
        return new SocialCard(body);
    }
}
