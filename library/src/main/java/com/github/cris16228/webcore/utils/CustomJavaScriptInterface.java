package com.github.cris16228.webcore.utils;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class CustomJavaScriptInterface {

    private final OnHtmlFetchedListener onHtmlFetchedListener;
    private WebView webView;

    public CustomJavaScriptInterface(OnHtmlFetchedListener onHtmlFetchedListener, WebView webView) {
        this.onHtmlFetchedListener = onHtmlFetchedListener;
        this.webView = webView;

    }

    @JavascriptInterface
    public void processHTML(String html) {
        webView.post(() -> onHtmlFetchedListener.onHtmlFetched(html));
    }
}
