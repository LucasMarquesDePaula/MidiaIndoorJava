package br.com.iandev.midiaindoor.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lucas on 13/11/2016.
 */
public class Downloader {

    public static int DEFAULT_BUFFER_LENGTH = 4096;
    public static int DEFAULT_CONNECT_TIMEOUT = 4096;

    private static enum RequestMethod {
        GET("GET"),
        POST("POST"),
        HEAD("HEAD"),
        OPTIONS("OPTIONS"),
        PUT("PUT"),
        DELETE("DELETE"),
        TRACE("TRACE");

        private final String method;

        private RequestMethod(String method) {
            this.method = method;
        }

        @Override
        public String toString() {
            return this.method;
        }
    }

    private URL url;
    private Map<String, String> requestHeaders;
    private Map<String, List<String>> responseHeaders;

    private OutputStream outputStream;
    private int bufferLength = DEFAULT_BUFFER_LENGTH;
    private int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
    private byte[] body = null;

    public Downloader() {
    }

    public Downloader setURL(URL url) {
        this.url = url;
        return this;
    }

    public Downloader setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
        return this;
    }

    public Downloader setRequestHeaders(Map<String, String> requestHeaders) {
        this.requestHeaders = new HashMap<>(requestHeaders);
        return this;
    }

    public Downloader setRequestHeader(Map.Entry<String, String> header) {
        return this.setRequestHeader(header.getKey(), header.getValue());
    }

    public Downloader setRequestHeader(String key, String value) {
        if (this.requestHeaders == null) {
            this.setRequestHeaders(new HashMap<>());
        }

        if (this.requestHeaders.containsKey(key)) {
            this.requestHeaders.remove(key);
        }

        this.requestHeaders.put(key, value);

        return this;
    }

    public Downloader setBufferLength(int bufferLength) {
        if (bufferLength <= 0) {
            throw new IllegalArgumentException("bufferLength must be greater than 0");
        }
        this.bufferLength = bufferLength;
        return this;
    }

    public Downloader setConnectTimeout(int connectTimeout) {
        if (connectTimeout <= 0) {
            throw new IllegalArgumentException("connectTimeout must be greater than 0");
        }
        this.connectTimeout = connectTimeout;
        return this;
    }

    private Downloader setResponseHeaders(Map<String, List<String>> responseHeaders) {
        this.responseHeaders = responseHeaders;
        return this;
    }

    public Map<String, List<String>> getResponseHeaders() {
        return this.responseHeaders;
    }

    public Downloader setBody(byte[] body) {
        this.body = body;
        return this;
    }

    public HttpURLConnection get() throws IOException {
        return this.run(RequestMethod.GET);
    }

    public HttpURLConnection post() throws IOException {
        return this.run(RequestMethod.POST);
    }

    public HttpURLConnection put() throws IOException {
        return this.run(RequestMethod.PUT);
    }

    private HttpURLConnection run(RequestMethod requestMethod) throws IOException {
        InputStream is = null;
        OutputStream os = outputStream;
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(connectTimeout);
            conn.setRequestMethod("GET");
            prepareRequestHeaders(conn);

            if (this.body != null) {
                conn.setDoOutput(true);
                conn.getOutputStream().write(this.body);
            }

            conn.connect();

            this.setResponseHeaders(conn.getHeaderFields());

            // download the file
            is = conn.getInputStream();

            copy(os, is);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (Exception ignored) {
            }

            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception ignored) {
            }

            try {
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception ignored) {
            }
        }
        return conn;
    }

    private void prepareRequestHeaders(URLConnection urlConnection) {
        if (requestHeaders != null) {
            requestHeaders.entrySet().forEach((property) -> {
                urlConnection.setRequestProperty(property.getKey(), property.getValue());
            });
        }
    }

    private void copy(OutputStream os, InputStream is) throws IOException {
        byte[] data = new byte[bufferLength];
        long total = 0;
        int count;
        while ((count = is.read(data)) != -1) {
            // allow canceling with back button
            total += count;
            os.write(data, 0, count);
        }
    }

}
