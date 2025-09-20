package org.codecraftlabs.shorturl.service;

import org.codecraftlabs.shorturl.service.repository.DatabaseException;
import org.codecraftlabs.shorturl.service.repository.ShortenedURL;
import org.codecraftlabs.shorturl.service.repository.URLShortnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Optional;

@Service
public class URLShortnerService {
    private final URLShortnerRepository urlShortnerRepository;

    @Autowired
    public URLShortnerService(@Nonnull URLShortnerRepository urlShortnerRepository) {
        this.urlShortnerRepository = urlShortnerRepository;
    }

    @Nonnull
    public String generateShortUrl(@Nonnull String originalUrl) {
        try {
            // Check if the URL is already shortened.
            var shortUrl = urlShortnerRepository.findShortenedUrl(originalUrl);
            if (shortUrl.isPresent()) {
                return shortUrl.get().shortUrl();
            }

            // Generates a short url version
            long urlId = urlShortnerRepository.getUrlId();
            Base62Converter converter = new Base62Converter();
            String convertedValue = converter.toBase62(urlId);

            // Saves the generated URL
            urlShortnerRepository.saveShortUrl(urlId, originalUrl, convertedValue);

            return convertedValue;
        } catch (DatabaseException exception) {
            throw new URLShorteningException("Fail to generate short URL", exception);
        }
    }

    @Nonnull
    public Optional<String> getOriginalUrl(@Nonnull String shortUrl) {
        // Check if the URL is already shortened.
        var item = urlShortnerRepository.findOriginalUrl(shortUrl);
        return item.map(ShortenedURL::url);
    }
}
