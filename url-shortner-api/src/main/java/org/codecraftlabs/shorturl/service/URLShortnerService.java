package org.codecraftlabs.shorturl.service;

import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

@Service
public class URLShortnerService {
    @Nonnull
    public String generateShortUrl(@Nonnull String originalUrl) {
        return "fake";
    }
}
