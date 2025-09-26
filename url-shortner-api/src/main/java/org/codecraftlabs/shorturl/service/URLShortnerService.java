package org.codecraftlabs.shorturl.service;

import org.codecraftlabs.shorturl.service.repository.DatabaseException;
import org.codecraftlabs.shorturl.service.repository.URLShortnerCachingRepository;
import org.codecraftlabs.shorturl.service.repository.URLShortnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Optional;

@Service
public class URLShortnerService {
    private static final Logger logger = LoggerFactory.getLogger(URLShortnerService.class);
    private final URLShortnerRepository urlShortnerRepository;
    private final URLShortnerCachingRepository urlShortnerCachingRepository;

    @Autowired
    public URLShortnerService(@Nonnull URLShortnerRepository urlShortnerRepository, @Nonnull URLShortnerCachingRepository urlShortnerCachingRepository) {
        this.urlShortnerRepository = urlShortnerRepository;
        this.urlShortnerCachingRepository = urlShortnerCachingRepository;
    }

    @Nonnull
    public String generateShortUrl(@Nonnull String originalUrl) {
        try {
            // Check if the URL is already shortened.
            var shortUrl = urlShortnerRepository.findShortenedUrl(originalUrl);
            if (shortUrl.isPresent()) {
                logger.info("URL already shortened. Returning existing item");
                return shortUrl.get().shortUrl();
            }
            // Generates a short url version
            var pair = generateShortUrl();

            // Saves the generated URL
            saveInDatabase(pair, originalUrl);

            return pair.getSecond();
        } catch (DatabaseException exception) {
            logger.error("Failed to generate a new short URL", exception);
            throw new URLShorteningException("Fail to generate short URL", exception);
        }
    }

    @Nonnull
    public Optional<String> getOriginalUrl(@Nonnull String shortUrl) {
        // Check if the URL is already shortened.
        return Optional.ofNullable(urlShortnerCachingRepository.getValue(shortUrl));
    }

    @Nonnull
    private Pair<Long, String> generateShortUrl() throws DatabaseException {
        long urlId = urlShortnerRepository.getUrlId();
        Base62Converter converter = new Base62Converter();
        String converted = converter.toBase62(urlId);
        return new Pair<>(urlId, converted);
    }

    private void saveInDatabase(@Nonnull Pair<Long, String> pair,
                                @Nonnull String originalUrl) throws DatabaseException {
        urlShortnerRepository.saveShortUrl(pair.getFirst(), originalUrl, pair.getSecond());

        // TODO: need to handle failures when saving the item in the cache
        urlShortnerCachingRepository.setValue(pair.getSecond(), originalUrl);
    }
}
