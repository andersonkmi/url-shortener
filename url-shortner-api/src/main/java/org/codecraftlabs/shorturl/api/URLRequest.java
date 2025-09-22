package org.codecraftlabs.shorturl.api;

public class URLRequest {
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "{'url': " + "'" + url + "'}";
    }
}
