package com.github.cris16228.webcore;

import com.github.cris16228.webcore.models.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClient {

    private String url;
    public static String POST = "POST";
    public static String GET = "GET";
    public static String DELETE = "DELETE";
    public static String PULL = "PULL";
    private String method = GET;
    private final Map<String, String> headers = new HashMap<>();
    private boolean followRedirects;
    private Document document;


    public HttpClient load(String url) {
        this.url = checkUrl(url);
        return this;
    }

    private String checkUrl(String _url) {
        if (!_url.startsWith("http://") && !_url.startsWith("https://")) {
            _url = "https://" + _url;
        }
        //Add more
        return _url;
    }

    public HttpClient setMethod(String method) {
        this.method = method;
        return this;
    }

    public HttpClient setHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public HttpClient followRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
        return this;
    }

    public Document build() {
        URL _url;
        try {
            _url = new URL(this.url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            AsyncUtil web = new AsyncUtil();
            web.execute(new AsyncUtil.onExecuteListener<Document>() {
                @Override
                public void preExecute() {

                }

                @Override
                public Document doInBackground() {
                    try {
                        List<String> history = new ArrayList<>();
                        history.add(url);
                        HttpURLConnection connection = (HttpURLConnection) _url.openConnection();
                        connection.setRequestMethod(method);
                        connection.setInstanceFollowRedirects(followRedirects);
                        for (Map.Entry<String, String> header : headers.entrySet()) {
                            connection.setRequestProperty(header.getKey(), header.getValue());
                        }
                        int responseCode = connection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP || responseCode == HttpURLConnection.HTTP_MOVED_PERM) {
                            if (!history.contains(connection.getHeaderField("Location"))) {
                                history.add(connection.getHeaderField("Location"));
                            }
                        }
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                public void postDelayed() {

                }
            }, result -> document = result);
            return document;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
