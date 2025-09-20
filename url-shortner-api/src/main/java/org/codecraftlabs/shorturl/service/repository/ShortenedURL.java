package org.codecraftlabs.shorturl.service.repository;

import java.util.Objects;

public record ShortenedURL(long id, String url, String shortUrl) {

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!getClass().equals(other.getClass())) {
            return false;
        }

        ShortenedURL instance = (ShortenedURL) other;
        return id == instance.id &&
                Objects.equals(url, instance.url) &&
                Objects.equals(shortUrl, instance.shortUrl);
    }

}
