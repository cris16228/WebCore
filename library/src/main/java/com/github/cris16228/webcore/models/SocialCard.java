package com.github.cris16228.webcore.models;

import android.text.TextUtils;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocialCard {

    private static final String OG_IMAGE_TAG = "<meta\\s+(?=[^>]*content\\s*=\\s*\"([^\"]*)\")[^>]*property\\s*=\\s*\"og:image\"[^>]*>|<meta\\s+(?=[^>]*property\\s*=\\s*\"og:image\")" +
            "[^>]*content\\s*=\\s*\"([^\"]*)\"[^>]*>";
    private static final String TWITTER_IMAGE_TAG = "<meta\\s+(?=[^>]*content\\s*=\\s*\"([^\"]*)\")[^>]*property\\s*=\\s*\"twitter:image\"[^>]*>|<meta\\s+(?=[^>]*property\\s*=\\s*\"twitter:image\")" +
            "[^>]*content\\s*=\\s*\"([^\"]*)\"[^>]*>";
    public static final String FAVICON = "<link[^>]+rel=[\"'](icon|shortcut icon)[\"'][^>]+href=[\"']([^\"']+)[\"']";
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
    private static final String TITLE_TAG = "<title.*?>(.*?)</title>";

    private final String body;
    private final URL _url;
    private String imageUrl;
    private String title;
    private String description;
    private String site;
    private String url;

    public SocialCard(String body, URL url) {
        this.body = body;
        this._url = url;
    }

    /**
     * It tries to find the image url in the meta tags used for social media:
     * It checks for og:image then twitter:image if the first fails
     *
     * @return {@imageUrl String} if found, empty otherwise
     */

    public String getImageUrl() {
        return getImageUrl(false);
    }

    /**
     * It tries to find the image url in the meta tags used for social media:
     * It checks for og:image then twitter:image if the first fails
     *
     * @param checkFavicon if true, tries to find the image url of the favicon - used as last resort if everything else fails
     * @return {@imageUrl String} if found, empty otherwise
     */
    public String getImageUrl(boolean checkFavicon) {

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
        //Tries to find the image url of the favicon if everything else fails
        if (TextUtils.isEmpty(imageUrl) && checkFavicon) {
            pattern = Pattern.compile(FAVICON);
            matcher = pattern.matcher(body);
            if (matcher.find()) {
                imageUrl = matcher.group(2);
                if (TextUtils.isEmpty(imageUrl)) {
                    if (!imageUrl.startsWith("http") || !imageUrl.startsWith("https")) {
                        imageUrl = _url.getProtocol() + "://" + _url.getHost() + imageUrl;
                    }
                }
            }
        }
        return imageUrl;
    }

    /**
     * It tries to find the title in the meta tags used for social media:
     * It checks for og:title then twitter:title if the first fails
     * If all this fails, return the <title></title> content
     *
     * @return {@title String} if found, empty otherwise
     */
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
        if (TextUtils.isEmpty(title)) {
            pattern = Pattern.compile(TITLE_TAG);
            matcher = pattern.matcher(body);
            if (matcher.find()) {
                title = matcher.group(1);
            }
        }
        return title;
    }

    /**
     * It tries to find the site name in the meta tags used for social media:
     * It checks for og:site
     *
     * @return {@site String} if found, empty otherwise
     */
    public String getSite() {
        Pattern pattern = Pattern.compile(OG_SITE_NAME_TAG);
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            site = matcher.group(1);
        }
        return site;
    }

    /**
     * It tries to find the site url in the meta tags used for social media:
     * It checks for og:url
     *
     * @return {@url String} if found, empty otherwise
     */
    public String getUrl() {
        Pattern pattern = Pattern.compile(OG_URL_TAG);
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            url = matcher.group(1);
        }
        return url;
    }

    /**
     * It tries to find the site name in the meta tags used for social media:
     * It checks for og:description then twitter:description if the first fails
     *
     * @return {@description String} if found, empty otherwise
     */
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
