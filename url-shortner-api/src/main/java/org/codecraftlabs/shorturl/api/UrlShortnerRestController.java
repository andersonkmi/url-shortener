package org.codecraftlabs.shorturl.api;

import org.codecraftlabs.shorturl.service.URLShorteningException;
import org.codecraftlabs.shorturl.service.URLShortnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Nonnull;
import java.net.URI;

@RestController
@RequestMapping("/url-shortner/v1")
public class UrlShortnerRestController {
    private static final Logger logger = LoggerFactory.getLogger(UrlShortnerRestController.class);
    private final URLShortnerService urlShortnerService;

    @Autowired
    public UrlShortnerRestController(@Nonnull URLShortnerService urlShortnerService) {
        this.urlShortnerService = urlShortnerService;
    }

    @PostMapping(value = "/url", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<URLResponse> createShortenedUrl(@RequestBody URLRequest urlRequest) {
        try {
            logger.info("Creating a short URL for {}", urlRequest.toString());
            String shortUrl = urlShortnerService.generateShortUrl(urlRequest.getUrl());
            URLResponse response = new URLResponse();
            response.setUrl(urlRequest.getUrl());
            response.setShortUrl(shortUrl);

            logger.info("Short URL created: '{}'", shortUrl);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (URLShorteningException exception) {
            logger.error("Fail to generate short URL", exception);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "URL not shortened", exception);
        } catch (Exception exception) {
            logger.error("An internal service error has occurred", exception);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal system error", exception);
        }
    }

    @GetMapping(value = "/url/{shortenedUrl}")
    public ResponseEntity<String> getUrl(@PathVariable String shortenedUrl) {
        logger.info("Returning original URL for {}", shortenedUrl);
        var originalUrl = this.urlShortnerService.getOriginalUrl(shortenedUrl);
        return originalUrl.
                <ResponseEntity<String>>map(s -> ResponseEntity.status(HttpStatus.FOUND).location(URI.create(s)).build())
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
