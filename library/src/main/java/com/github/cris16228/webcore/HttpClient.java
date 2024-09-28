package com.github.cris16228.webcore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.util.Patterns;
import android.webkit.WebView;

import com.github.cris16228.webcore.models.Document;
import com.github.cris16228.webcore.utils.CustomJavaScriptInterface;
import com.github.cris16228.webcore.utils.CustomWebClient;
import com.github.cris16228.webcore.utils.OnHtmlFetchedListener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpClient {

    private String url;
    public static String POST = "POST";
    public static String GET = "GET";
    public static String DELETE = "DELETE";
    public static String PULL = "PULL";
    private String method = GET;
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> params = new HashMap<>();
    private boolean followRedirects;
    private Document document;
    private int maxRetries = -1;
    private int currentRedirects = 0;
    private static final String urlRegex = "(https?://[\\w\\-\\.]+(:\\d+)?(/([\\w/_\\-\\.]*\\??[\\w=&%\\-\\.]*#?[\\w\\-]*)?)?)";
    private String userAgent;
    private String defaultUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) rv:130.0) Gecko/20100101 Firefox/130.0";
    private boolean legacy = false;
    private Context context;

    private OnDocumentListener onDocumentListener;

    public HttpClient(Context context) {
        this.context = context;
    }

    public HttpClient() {
    }

    public HttpClient load(String url) {
        this.url = checkUrl(url);
        return this;
    }

    private String checkUrl(String _url) {
        Pattern pattern = Pattern.compile(urlRegex);
        Matcher matcher = pattern.matcher(_url);
        if (matcher.find()) {
            if (!_url.startsWith("http://") && !_url.startsWith("https://")) {
                _url = "https://" + _url;
            }
            if (!_url.endsWith("/")) {
                _url += "/";
            }
            if (!Patterns.WEB_URL.matcher(_url).matches()) {
                return null;
            }
            return _url;
        }
        return null;
    }

    public HttpClient setLegacy(boolean legacy) {
        this.legacy = legacy;
        return this;
    }

    public HttpClient getDefaultUserAgent() {
        this.userAgent = defaultUserAgent;
        return this;
    }

    public HttpClient setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public HttpClient setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
        return this;
    }

    public HttpClient setMethod(String method) {
        this.method = method;
        return this;
    }

    public HttpClient addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public HttpClient addParam(String key, String value) {
        params.put(key, value);
        return this;
    }

    public HttpClient followRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
        return this;
    }

    public HttpClient addOnCompleteListener(OnDocumentListener onDocumentListener) {
        this.onDocumentListener = onDocumentListener;
        return this;
    }

    public void build() {
        URL _url;
        try {
            _url = new URL(this.url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        try {
            WebCoreProcess web = new WebCoreProcess();
            web.execute(new WebCoreProcess.onExecuteListener<Document>() {
                @Override
                public void preExecute() {
                    if (!legacy) {
                        connection();
                        return;
                    }
                }

                @Override
                public Document doInBackground() {
                    if (legacy) {
                        return legacyConnection();
                    }
                    return null;
                }

                @Override
                public void postDelayed() {
                }
            }, result ->

            {
                if (onDocumentListener != null) {
                    onDocumentListener.onComplete(result);
                }
            });
        } catch (
                InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void connection() {
        if (context == null) {
            throw new RuntimeException("Context is null. Please user new HttpClient(context) instead!");
        }
        WebView webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new CustomWebClient());
        OnHtmlFetchedListener onHtmlFetchedListener = html -> {
            try {
                document = new Document(html, new URL(webView.getUrl()));
                System.out.println(onDocumentListener == null);
                System.out.println(document.getUrl());
                System.out.println(document.getStatusCode());
                if (onDocumentListener != null) {
                    onDocumentListener.onComplete(document);
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        };

        webView.addJavascriptInterface(new CustomJavaScriptInterface(onHtmlFetchedListener), "HTMLOUT");
        webView.loadUrl(url);
    }

    private String setPostData(Map<String, String> params) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(param.getKey(), StandardCharsets.UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(param.getValue(), StandardCharsets.UTF_8));
        }
        return result.toString();
    }

    private Document legacyConnection() {
        try {
            boolean redirect;
            HttpURLConnection connection;
            int responseCode;
            List<String> history = new ArrayList<>();
            history.add(url);
            URL _url;
            if ("GET".equalsIgnoreCase(method)) {
                _url = new URL(setGetUrl(url, params));
            } else {
                _url = new URL(url);
            }
            do {
                connection = (HttpURLConnection) _url.openConnection();
                connection.setRequestMethod(method);
                connection.setInstanceFollowRedirects(false);
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    connection.setRequestProperty(header.getKey(), header.getValue());
                }
                if (userAgent != null) {
                    connection.setRequestProperty("User-Agent", userAgent);
                }

                if ("POST".equalsIgnoreCase(method)) {
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    try {
                        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                        out.writeBytes(setPostData(params));
                        out.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                responseCode = connection.getResponseCode();
                redirect = (responseCode == HttpURLConnection.HTTP_MOVED_TEMP || responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_SEE_OTHER);
                if (redirect && followRedirects) {
                    currentRedirects++;
                    String newUrl = connection.getHeaderField("Location");
                    if (newUrl != null) {
                        if (!newUrl.startsWith("http") && !newUrl.startsWith("https")) {
                            newUrl = _url.getProtocol() + "://" + _url.getHost() + newUrl;
                        }
                        if (maxRetries > 0) {
                            Log.i("HttpClient", "Redirecting from " + _url + " to " + newUrl + " (" + currentRedirects + "/" + maxRetries + ")");
                            if (currentRedirects >= maxRetries) {
                                Log.e("HttpClient", "Max retries reached");
                                break;
                            }
                        } else {
                            Log.i("HttpClient", "Redirecting from " + _url + " to " + newUrl);
                        }
                        if (!history.contains(newUrl)) {
                            history.add(newUrl);
                            _url = new URL(newUrl);
                        }
                    } else {
                        break;
                    }
                }
            }
            while (redirect && followRedirects);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            if (!history.contains(connection.getURL().toString())) {
                history.add(connection.getURL().toString());
            }
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return new Document(responseCode, connection.getHeaderFields(), response.toString(), connection.getURL(), history);
        } catch (
                Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String setGetUrl(String url, Map<String, String> params) {
        if (params.isEmpty()) {
            return url;
        }
        StringBuilder result = new StringBuilder(url);
        result.append(url.contains("?") ? "&" : "?");
        boolean first = true;
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(param.getKey(), StandardCharsets.UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(param.getValue(), StandardCharsets.UTF_8));
        }
        result.setLength(result.length() - 1);
        return result.toString();
    }

    public interface OnDocumentListener {
        void onComplete(Document document);
    }
}
