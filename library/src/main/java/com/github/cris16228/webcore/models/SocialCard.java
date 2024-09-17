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
    private static final String OG_SITE_NAME_TAG = "<meta\\s+(?=[^>]*content\\s*=\\s*\"([^\"]*)\")[^>]*property\\s*=\\s*\"og:site_name\"[^>]*>|<meta\\s+(?=[^>]*property\\s*=\\s*\"og:site_name\")" +
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

        //Tries to find the image url in the meta tag og:image
        Pattern pattern = Pattern.compile(OG_IMAGE_TAG);
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            imageUrl = matcher.group(1);
        }
        //Tries to find the image url in the meta tag twitter:image if the first one fails
        if (TextUtils.isEmpty(imageUrl)) {
            pattern = Pattern.compile(TWITTER_IMAGE_TAG);
            matcher = pattern.matcher(body);
            if (matcher.find()) {
                imageUrl = matcher.group(1);
            }
        }
        if (TextUtils.isEmpty(imageUrl)) {

        }
        return imageUrl;
    }

    public String getTitle() {

        // Tries to find the title url in the meta tag og:image
        Pattern pattern = Pattern.compile(OG_TITLE_TAG);
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            title = matcher.group(1);
        }
        //Tries to find the title url in the meta tag twitter:title if the first one fails
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
        // Tries to find the site name in the meta tag og:site
        Pattern pattern = Pattern.compile(OG_SITE_NAME_TAG);
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            site = matcher.group(1);
        }
        return site;
    }

    public String getUrl() {
        // Tries to find the site url in the meta tag og:url
        Pattern pattern = Pattern.compile(OG_URL_TAG);
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            url = matcher.group(1);
        }
        return url;
    }

    public String getDescription() {
        // Tries to find the site description in the meta tag og:description
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
