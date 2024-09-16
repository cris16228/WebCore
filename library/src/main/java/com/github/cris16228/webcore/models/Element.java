package com.github.cris16228.webcore.models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Element {

    private String html;

    public Element(String html) {
        this.html = html;
    }

    public String get(String key) {
        switch (key) {
            case "text":
                return getTextContent();
            default:
                return getAttribute(key);
        }
    }

    private String getTextContent() {
        Pattern pattern = Pattern.compile(">([^<]+)<");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String getAttribute(String attributeKey) {
        String regex = attributeKey + "=['\"]([^'\"]+)['\"]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public String getHtml() {
        return html;
    }
}
