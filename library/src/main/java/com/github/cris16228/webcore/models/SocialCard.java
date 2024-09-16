package com.github.cris16228.webcore.models;

import android.text.TextUtils;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocialCard {

    private static final String OG_IMAGE_TAG = "<meta\\s+(?=[^>]*content\\s*=\\s*\"([^\"]*)\")[^>]*property\\s*=\\s*\"og:image\"[^>]*>|<meta\\s+(?=[^>]*property\\s*=\\s*\"og:image\")[^>]*content\\s*=\\s*\"([^\"]*)\"[^>]*>";
    private static final String TWITTER_IMAGE_TAG = "<meta\\s+(?=[^>]*content\\s*=\\s*\"([^\"]*)\")[^>]*property\\s*=\\s*\"twitter:image\"[^>]*>|<meta\\s+(?=[^>]*property\\s*=\\s*\"twitter:image\")[^>]*content\\s*=\\s*\"([^\"]*)\"[^>]*>";
    private static final String OG_TITLE_TAG = "<meta\\s+(?=[^>]*content\\s*=\\s*\"([^\"]*)\")[^>]*property\\s*=\\s*\"og:title\"[^>]*>|<meta\\s+(?=[^>]*property\\s*=\\s*\"og:title\")[^>]*content\\s*=\\s*\"([^\"]*)\"[^>]*>";
    private static final String OG_DESCRIPTION_TAG = "<meta\\s+(?=[^>]*content\\s*=\\s*\"([^\"]*)\")[^>]*property\\s*=\\s*\"og:description\"[^>]*>|<meta\\s+(?=[^>]*property\\s*=\\s*\"og:description\")[^>]*content\\s*=\\s*\"([^\"]*)\"[^>]*>";

    private String body;
    private String imageUrl;

    public SocialCard(String body) {
        this.body = body;
    }

    public String getImageUrl() {
        Pattern ogImage = Pattern.compile(OG_IMAGE_TAG);
        Matcher matcher = ogImage.matcher(body);
        if (matcher.find()) {
            imageUrl = matcher.group(1);
        }
        if (TextUtils.isEmpty(imageUrl)) {
            Pattern twitterImage = Pattern.compile(TWITTER_IMAGE_TAG);
            Matcher tMatcher = twitterImage.matcher(body);
            if (tMatcher.find()) {
                imageUrl = tMatcher.group(1);
            }
        }
        return imageUrl;
    }

    public String getTitle() {
        String title = "";
        Pattern pattern = Pattern.compile(OG_TITLE_TAG);
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            title = matcher.group(1);
        }
        return title;
    }

    public String getDescription() {
        String title = "";
        Pattern pattern = Pattern.compile(OG_DESCRIPTION_TAG);
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            title = matcher.group(1);
        }
        return title;
    }
}
