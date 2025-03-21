package com.ss.infrastructure.util;
import java.io.FileInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.StringJoiner;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.lang3.StringUtils;

public class HttpClientUtils {

    public static HttpClient createHttpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    public static HttpClient createHttpClient(String certPath) throws Exception {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .sslContext(trustSpecificCertificate(certPath))
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    private static SSLContext trustSpecificCertificate(String certPath) throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileInputStream certInputStream = new FileInputStream(certPath);
        X509Certificate certificate = (X509Certificate) cf.generateCertificate(certInputStream);
        certInputStream.close();

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        keyStore.setCertificateEntry("trustedCert", certificate);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());

        return sslContext;
    }

    private static Builder createDefaultHeader() {
        return HttpRequest.newBuilder().header("Content-Type", "application/json; charset=utf-8");
    }

    public static HttpResponse<String> httpGet(String authorization, String url,
            Map<String, String> param, HttpClient client)
            throws Exception {
        StringJoiner queryString = new StringJoiner("&");
        for (Map.Entry<String, String> entry : param.entrySet()) {
            queryString.add(entry.getKey() + "=" + entry.getValue());
        }

        URI uri = new URI(url + "?" + queryString.toString());
        HttpRequest.Builder requestBuilder = createDefaultHeader()
                .uri(uri)
                .GET();

        if (StringUtils.isNotEmpty(authorization)) {
            requestBuilder.header("Authorization", authorization);
        }

        HttpRequest request = requestBuilder.build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> httpPost(String authorization, String url, String body, HttpClient client)
            throws Exception {
        URI uri = new URI(url);
        HttpRequest.Builder requestBuilder = createDefaultHeader()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(body));

        if (StringUtils.isNotEmpty(authorization)) {
            requestBuilder.header("Authorization", authorization);
        }

        HttpRequest request = requestBuilder.build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> httpPut(String authorization, String url, String body, HttpClient client)
            throws Exception {
        URI uri = new URI(url);
        HttpRequest.Builder requestBuilder = createDefaultHeader()
                .uri(uri)
                .PUT(HttpRequest.BodyPublishers.ofString(body));

        if (StringUtils.isNotEmpty(authorization)) {
            requestBuilder.header("Authorization", authorization);
        }

        HttpRequest request = requestBuilder.build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> httpDelete(String authorization, String url, Map<String, String> param,
            HttpClient client)
            throws Exception {
        StringJoiner queryString = new StringJoiner("&");
        for (Map.Entry<String, String> entry : param.entrySet()) {
            queryString.add(entry.getKey() + "=" + entry.getValue());
        }

        URI uri = new URI(url + "?" + queryString.toString());
        HttpRequest.Builder requestBuilder = createDefaultHeader()
                .uri(uri)
                .DELETE();

        if (StringUtils.isNotEmpty(authorization)) {
            requestBuilder.header("Authorization", authorization);
        }

        HttpRequest request = requestBuilder.build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

}
