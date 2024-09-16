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

    public Document(int statusCode, Map<String, List<String>> headers, String body, URL url) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
        this.url = url;
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
        String regex = "<([a-zA-Z0-9]+)([^>]*\\sid=['\"]" + id + "['\"][^>]*)>(.*?)</\\1>";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            return new Element(matcher.group(0));
        }
        return null;
    }

    public List<Element> getElementsById(String id) {
        List<Element> elements = new ArrayList<>();
        String regex = "<([a-zA-Z0-9]+)([^>]*\\sid=['\"]" + id + "['\"][^>]*)>(.*?)</\\1>";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(body);
        while (matcher.find()) {
            elements.add(new Element(matcher.group(0)));
        }
        return elements;
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

    public List<Element> getElementsByClass(String className) {
        List<Element> elements = new ArrayList<>();
        String regex = "<([a-zA-Z0-9]+)([^>]*\\sclass=['\"]([^'\"]*\\s)?" + className + "(\\s[^'\"]*)?['\"][^>]*)>(.*?)</\\1>";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(body);
        while (matcher.find()) {
            elements.add(new Element(matcher.group(0)));
        }
        return elements;
    }

    public Element find(String id, String className) {
        String regex = "<([a-zA-Z0-9]+)\\b[^>]*\\bid=['\"]" + id + "['\"][^>]*\\bclass=['\"][^'\"]*" + className + "[^'\"]*['\"][^>]*>(.*?)</\\1>";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            return new Element(matcher.group(0));
        }
        return null;
    }

    public List<Element> findAll(String id, String className) {
        List<Element> elements = new ArrayList<>();
        String regex = "<([a-zA-Z0-9]+)\\b[^>]*\\bid=['\"]" + id + "['\"][^>]*\\bclass=['\"][^'\"]*" + className + "[^'\"]*['\"][^>]*>(.*?)</\\1>";
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
