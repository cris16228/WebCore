package com.github.cris16228.webcore.models;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Element {

    private final String content;
    private final Map<String, String> attributes = new HashMap<>();
    private String tagName;
    private String id;
    private String className;

    public Element(String content) {
        this.content = content;
        parseTag();
    }

    public String getContent() {
        return content;
    }

    public String getTagName() {
        return tagName;
    }

    public String getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    private void parseTag() {
        Pattern tagPattern = Pattern.compile("<\\s*(\\w+)");
        Matcher tagMatcher = tagPattern.matcher(content);
        if (tagMatcher.find()) {
            tagName = tagMatcher.group(1);
        } else {
            tagName = null;
        }
        Pattern classPattern = Pattern.compile("\\.(\\w+)");
        Matcher classMatcher = classPattern.matcher(content);
        if (classMatcher.find()) {
            className = classMatcher.group(1);
        } else {
            className = null;
        }

        Pattern attribubePattern = Pattern.compile("(\\w+)=['\"]([^'\"]+)['\"]");
        Matcher attrMatcher = attribubePattern.matcher(content);
        while (attrMatcher.find()) {
            attributes.put(attrMatcher.group(1), attrMatcher.group(2));
            if (attrMatcher.group(1).equalsIgnoreCase("id")) {
                id = attrMatcher.group(2);
            }
            if (attrMatcher.group(1).equalsIgnoreCase("class")) {
                className = attrMatcher.group(2);
            }
        }
    }

    public String get(String key) {
        if (TextUtils.isEmpty(content))
            return "empty";
        if (key.equals("text")) {
            return getTextContent();
        }
        return getAttribute(key);
    }

    private String getTextContent() {
        Pattern pattern = Pattern.compile(">([^<]+)<");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public boolean hasAttribute(String attributeKey) {
        return attributes.containsKey(attributeKey);
    }

    private String getAttribute(String attributeKey) {
        return attributes.get(attributeKey);
    }
}
