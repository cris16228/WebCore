package com.github.cris16228.webcore;

import com.github.cris16228.webcore.models.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpClient {

    private String url;
    public static String POST = "POST";
    public static String GET = "GET";
    public static String DELETE = "DELETE";
    public static String PULL = "PULL";
    private String method = GET;
    private Map<String, String> headers = new HashMap<>();

    public HttpClient load(String url) {
        this.url = url;
        return this;
    }

    public HttpClient setMethod(String method) {
        this.method = method;
        return this;
    }

    public HttpClient setHeader(String key, String value) {
        headers.put(key, value);
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
            Document document = web.execute(new AsyncUtil.onExecuteListener<Document>() {
                @Override
                public void preExecute() {

                }

                @Override
                public Document doInBackground() {
                    try {
                        HttpURLConnection connection = (HttpURLConnection) _url.openConnection();
                        connection.setRequestMethod(method);

                        for (Map.Entry<String, String> header : headers.entrySet()) {
                            connection.setRequestProperty(header.getKey(), header.getValue());
                        }
                        int responseCode = connection.getResponseCode();

                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        return new Document(responseCode, connection.getHeaderFields(), response.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                public void postDelayed() {

                }
            });
            return document;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
