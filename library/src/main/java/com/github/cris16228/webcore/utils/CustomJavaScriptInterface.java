package com.github.cris16228.webcore.utils;

import android.webkit.JavascriptInterface;

public class CustomJavaScriptInterface {

    private final OnHtmlFetchedListener onHtmlFetchedListener;

    public CustomJavaScriptInterface(OnHtmlFetchedListener onHtmlFetchedListener) {
        this.onHtmlFetchedListener = onHtmlFetchedListener;
    }

    @JavascriptInterface
    public void processHTML(String html) {
        onHtmlFetchedListener.onHtmlFetched(html);
    }
}
