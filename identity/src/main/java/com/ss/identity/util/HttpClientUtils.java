package com.ss.identity.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;

public class HttpClientUtils {

    private static HttpClient createHttpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    private static Builder createDefaultHeader() {
        return HttpRequest.newBuilder().header("Content-Type", "application/json; charset=utf-8");
    }

    public static HttpResponse<String> httpGet(boolean isJwt, String authorization, String url,
            Map<String, String> param)
            throws Exception {
        HttpClient client = createHttpClient();

        StringJoiner queryString = new StringJoiner("&");
        for (Map.Entry<String, String> entry : param.entrySet()) {
            queryString.add(entry.getKey() + "=" + entry.getValue());
        }

        URI uri = new URI(url + "?" + queryString.toString());
        HttpRequest.Builder requestBuilder = createDefaultHeader()
                .uri(uri)
                .GET();

        if (StringUtils.isNotEmpty(authorization)) {
            if (isJwt) {
                requestBuilder.header("Authorization", "Bearer " + authorization);
            } else {
                String encodedAuth = Base64.getEncoder().encodeToString(authorization.getBytes());
                requestBuilder.header("Authorization", "Basic " + encodedAuth);
            }
        }

        HttpRequest request = requestBuilder.build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> httpPost(boolean isJwt, String authorization, String url, String body)
            throws Exception {
        HttpClient client = createHttpClient();
        URI uri = new URI(url);
        HttpRequest.Builder requestBuilder = createDefaultHeader()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(body));

        if (StringUtils.isNotEmpty(authorization)) {
            if (isJwt) {
                requestBuilder.header("Authorization", "Bearer " + authorization);
            } else {
                String encodedAuth = Base64.getEncoder().encodeToString(authorization.getBytes());
                requestBuilder.header("Authorization", "Basic " + encodedAuth);
            }
        }

        HttpRequest request = requestBuilder.build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
