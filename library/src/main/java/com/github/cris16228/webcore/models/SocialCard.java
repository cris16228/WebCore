package com.github.cris16228.webcore.models;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocialCard {

    private static final String OG_IMAGE_TAG = "<meta\\s+(?=[^>]*content\\s*=\\s*\"([^\"]*)\")[^>]*property\\s*=\\s*\"og:image\"[^>]*>|<meta\\s+(?=[^>]*property\\s*=\\s*\"og:image\")" +
            "[^>]*content\\s*=\\s*\"([^\"]*)\"[^>]*>";
    private static final String TWITTER_IMAGE_TAG = "<meta\\s+(?=[^>]*content\\s*=\\s*\"([^\"]*)\")[^>]*property\\s*=\\s*\"twitter:image\"[^>]*>|<meta\\s+(?=[^>]*property\\s*=\\s*\"twitter:image\")" +
            "[^>]*content\\s*=\\s*\"([^\"]*)\"[^>]*>";
    private static final String OG_TITLE_TAG = "<meta\\s+(?=[^>]*content\\s*=\\s*\"([^\"]*)\")[^>]*property\\s*=\\s*\"og:title\"[^>]*>|<meta\\s+(?=[^>]*property\\s*=\\s*\"og:title\")" +
            "[^>]*content\\s*=\\s*\"([^\"]*)\"[^>]*>";
    private static final String TWITTER_TITLE_TAG = "<meta\\s+(?=[^>]*content\\s*=\\s*\"([^\"]*)\")[^>]*property\\s*=\\s*\"twitter:title\"[^>]*>|<meta\\s+(?=[^>]*property\\s*=\\s*\"twitter:title\")" +
            "[^>]*content\\s*=\\s*\"([^\"]*)\"[^>]*>";
    private static final String OG_DESCRIPTION_TAG = "<meta\\s+(?=[^>]*content\\s*=\\s*\"([^\"]*)\")[^>]*property\\s*=\\s*\"og:description\"[^>]*>|<meta\\s+" +
            "(?=[^>]*property\\s*=\\s*\"og:description\")[^>]*content\\s*=\\s*\"([^\"]*)\"[^>]*>";
    private static final String TWITTER_DESCRIPTION_TAG = "<meta\\s+(?=[^>]*content\\s*=\\s*\"([^\"]*)\")[^>]*property\\s*=\\s*\"twitter:description\"[^>]*>|<meta\\s+" +
            "(?=[^>]*property\\s*=\\s*\"twitter:description\")[^>]*content\\s*=\\s*\"([^\"]*)\"[^>]*>";
    private static final String OG_SITE_TAG = "<meta\\s+(?=[^>]*content\\s*=\\s*\"([^\"]*)\")[^>]*property\\s*=\\s*\"og:site\"[^>]*>|<meta\\s+(?=[^>]*property\\s*=\\s*\"og:site\")" +
            "[^>]*content\\s*=\\s*\"([^\"]*)\"[^>]*>";
    private static final String OG_URL_TAG = "<meta\\s+(?=[^>]*content\\s*=\\s*\"([^\"]*)\")[^>]*property\\s*=\\s*\"og:url\"[^>]*>|<meta\\s+(?=[^>]*property\\s*=\\s*\"og:url\")" +
            "[^>]*content\\s*=\\s*\"([^\"]*)\"[^>]*>";

    private final String body;
    private String imageUrl;
    private String title;
    private String description;
    private String site;
    private String url;

    public SocialCard(String body) {
        this.body = body;
    }

    public String getImageUrl() {
        Pattern pattern = Pattern.compile(OG_IMAGE_TAG);
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            imageUrl = matcher.group(1);
        }
        if (TextUtils.isEmpty(imageUrl)) {
            pattern = Pattern.compile(TWITTER_IMAGE_TAG);
            matcher = pattern.matcher(body);
            if (matcher.find()) {
                imageUrl = matcher.group(1);
            }
        }
        return imageUrl;
    }

    public String getTitle() {
        Pattern pattern = Pattern.compile(OG_TITLE_TAG);
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            title = matcher.group(1);
        }
        if (TextUtils.isEmpty(title)) {
            pattern = Pattern.compile(TWITTER_TITLE_TAG);
            matcher = pattern.matcher(body);
            if (matcher.find()) {
                title = matcher.group(1);
            }
        }
        return title;
    }

    public String getSite() {
        Pattern pattern = Pattern.compile(OG_SITE_TAG);
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            site = matcher.group(1);
        }
        return site;
    }

    public String getUrl() {
        Pattern pattern = Pattern.compile(OG_URL_TAG);
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            url = matcher.group(1);
        }
        return url;
    }

    public String getDescription() {
        Pattern pattern = Pattern.compile(OG_DESCRIPTION_TAG);
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            description = matcher.group(1);
        }
        if (TextUtils.isEmpty(description)) {
            pattern = Pattern.compile(TWITTER_DESCRIPTION_TAG);
            matcher = pattern.matcher(body);
            if (matcher.find()) {
                description = matcher.group(1);
            }
        }
        return description;
    }
}
