package com.github.cris16228.webcore.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Document {

    private int statusCode;
    private Map<String, List<String>> headers;
    private String body;

    public Document(int statusCode, Map<String, List<String>> headers, String body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
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
        String regex = "<([a-zA-Z0-9]+)([^>]*\\sid=['\"]" + id + "['\"[^>]*)>(.*?)</\\1>";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            return new Element(matcher.group(0));
        }
        return null;
    }

    public Element getElementByClass(String className) {
        String regex = "<([a-zA-Z0-9]+)([^>]*\\sclass=['\"]([^'\"]*\\s)?" + className + "(\\s[^'\"]*)?['\"][^>]*)>(.*?)</\\1>";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            return new Element(matcher.group(0));
        }
        return null;
    }

    public Element find(String id, String className) {
        String regex = "<([a-zA-Z0-9]+)([^>]*\\sid=['\"]" + id + "['\"][^>]*\\sclass=['\"]([^'\"]*\\s)?" + className + "(\\s[^'\"]*)?['\"][^>]*)>(.*?)</\\1>";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            return new Element(matcher.group(0));
        }
        return null;
    }

    public List<Element> findAll(String id, String className) {
        List<Element> elements = new ArrayList<>();
        String regex = "<([a-zA-Z0-9]+)([^>]*\\sid=['\"]" + id + "['\"][^>]*\\sclass=['\"]([^'\"]*\\s)?" + className + "(\\s[^'\"]*)?['\"][^>]*)>(.*?)</\\1>";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
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
